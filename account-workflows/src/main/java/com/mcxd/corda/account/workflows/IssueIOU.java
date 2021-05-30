package com.mcxd.corda.account.workflows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.account.states.IOUState;
import com.mcxd.corda.account.contracts.IOUContract;
import com.r3.corda.lib.accounts.contracts.states.AccountInfo;
import com.r3.corda.lib.accounts.workflows.flows.RequestKeyForAccount;
import com.r3.corda.lib.accounts.workflows.services.AccountService;
import com.r3.corda.lib.accounts.workflows.services.KeyManagementBackedAccountService;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.crypto.TransactionSignature;
import net.corda.core.flows.*;
import net.corda.core.identity.AnonymousParty;
import net.corda.core.identity.PartyAndCertificate;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/*
  The flows defined in this project is 'Account-Oriented', meaning that they assume that Identities involved
  are accounts. Moreover, they assume that relevant accounts are already in the node that initiates the flow.
 */
public class IssueIOU {
    @StartableByRPC
    @InitiatingFlow
    static public class Initiator extends FlowLogic<String> {
        private static final ProgressTracker.Step RESOLVE_ACCOUNTS = new ProgressTracker.Step("Resolving Accounts");
        private static final ProgressTracker.Step PREPARE_KEYS = new ProgressTracker.Step("Preparing Keys");
        private static final ProgressTracker.Step BUILD_TRANSACTION = new ProgressTracker.Step("Building Transaction");
        private static final ProgressTracker.Step COLLECT_SIGNATURE = new ProgressTracker.Step("Collecting Signature");
        private static final ProgressTracker.Step FINALISATION = new ProgressTracker.Step("Finalising"){
            @Override
            public ProgressTracker childProgressTracker(){
                return FinalityFlow.Companion.tracker();
            }
        };

        private final ProgressTracker progressTracker = new ProgressTracker(
                RESOLVE_ACCOUNTS,
                PREPARE_KEYS,
                BUILD_TRANSACTION,
                COLLECT_SIGNATURE,
                FINALISATION
        );

        private final String lender;
        private final String borrower;
        private final long amount;

        public Initiator(String lender, String borrower, long amount) {
            this.lender = lender;
            this.borrower = borrower;
            this.amount = amount;
        }

        @Override
        @Suspendable
        public String call() throws FlowException {
            try {
                // Resolve Accounts
                // keep a service on the stack is generally not a good idea
                progressTracker.setCurrentStep(RESOLVE_ACCOUNTS);
                AccountService accountService = getServiceHub().cordaService(KeyManagementBackedAccountService.class);

                // Generate keys used for transaction
                progressTracker.setCurrentStep(PREPARE_KEYS);
                AccountInfo lenderInfo = accountService.accountInfo(this.lender).get(0).getState().getData();
                AnonymousParty newLender = subFlow(new RequestKeyForAccount(lenderInfo));

                AccountInfo borrowerInfo = accountService.accountInfo(this.borrower).get(0).getState().getData();
                AnonymousParty newBorrower = subFlow(new RequestKeyForAccount(borrowerInfo));

                progressTracker.setCurrentStep(BUILD_TRANSACTION);
                // Create IOUState
                IOUState iouState = new IOUState(newLender, newBorrower, this.amount, new UniqueIdentifier());

                //  Build transaction
                TransactionBuilder txb =
                        new TransactionBuilder(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));

                txb.addOutputState(iouState)
                        .addCommand(new IOUContract.Commands.Create(), Arrays.asList(newLender.getOwningKey(),
                                newBorrower.getOwningKey()));



                /*Although technically, based on the assumption that lender and borrower are in the same node, we
                could sign them both without invoking a CollectSignaturesFlow. However, this does not resemble
                real-world scenario. Hence we need to invoke relevant flows to gather signatures for additional
                checks. For example, borrower may not agree with this transaction */

                // Lender sign. We assume that lender initiates this flow. Initiation means that the account
                // automatically signs the initiated transaction.
                SignedTransaction lenderSigned = getServiceHub().signInitialTransaction(
                        txb,
                        newLender.getOwningKey()
                );

                progressTracker.setCurrentStep(COLLECT_SIGNATURE);
                // Request Borrower Signature
                // This flow gets and verifies required signature via the flowSession
                FlowSession borrowerSession = initiateFlow(borrowerInfo.getHost());
                TransactionSignature borrowerSignature = subFlow(new CollectSignatureFlow(
                        lenderSigned,
                       borrowerSession,
                        Arrays.asList(newBorrower.getOwningKey()))).get(0);

                SignedTransaction fullySigned = lenderSigned.withAdditionalSignature(borrowerSignature);

                // Finalise
                progressTracker.setCurrentStep(FINALISATION);

                subFlow(new FinalityFlow(fullySigned,
                        //Tricky, counterparty is actually ourself. This flow forbids session with oneself.
                        Arrays.asList(),
                        progressTracker.getCurrentStep().childProgressTracker()));

                return iouState.getLinearId().toString(); // provide information about the state
            }catch (Exception e){
                throw new FlowException("Flow cannot finish: " + e.getMessage());
            }
        }

        @Override
        public ProgressTracker getProgressTracker(){
            return this.progressTracker;
        }
    }

    @InitiatedBy(Initiator.class)
    @Suspendable
    static public class Responder extends FlowLogic<SignedTransaction>{
        private final FlowSession lenderSession;

        public Responder(FlowSession lenderSession){
            this.lenderSession = lenderSession;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            subFlow(new SignTransactionFlow(lenderSession) {
                @Override
                protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                    // Customized logic to verify a transaction
                    // How to add explict party intervention
                }
            });

            return subFlow(new ReceiveFinalityFlow(lenderSession));
        }
    }
}
