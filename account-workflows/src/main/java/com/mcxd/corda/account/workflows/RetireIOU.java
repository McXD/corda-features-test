package com.mcxd.corda.account.workflows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.account.contracts.IOUContract;
import com.mcxd.corda.account.states.IOUState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.crypto.TransactionSignature;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RetireIOU {
    @StartableByRPC
    @InitiatingFlow
    public static class Initiator extends FlowLogic<SignedTransaction> {
        private static final ProgressTracker.Step QUERY_VAULT = new ProgressTracker.Step("Querying vault");
        private static final ProgressTracker.Step BUILD_TRANSACTION = new ProgressTracker.Step("Building Transaction");
        private static final ProgressTracker.Step COLLECT_SIGNATURE = new ProgressTracker.Step("Collecting Signature");
        private static final ProgressTracker.Step FINALISATION = new ProgressTracker.Step("Finalising"){
            @Override
            public ProgressTracker childProgressTracker(){
                return FinalityFlow.Companion.tracker();
            }
        };

        private final ProgressTracker progressTracker = new ProgressTracker(
                QUERY_VAULT,
                BUILD_TRANSACTION,
                COLLECT_SIGNATURE,
                FINALISATION
        );

        @Override
        public ProgressTracker getProgressTracker(){
            return this.progressTracker;
        }

        private String uid;
        public Initiator(String uid){
            this.uid = uid;
        }

        @Override
        @Suspendable
        public SignedTransaction call() throws FlowException {
            try {
                progressTracker.setCurrentStep(QUERY_VAULT);
                List<StateAndRef<IOUState>> states =
                        getServiceHub().getVaultService().queryBy(IOUState.class).getStates();

                states = states.stream()
                        .filter(state -> state.getState().getData().getLinearId().toString().equals(this.uid)).collect(Collectors.toList());

                if (states.size() == 0) throw new FlowException("Cannot find this state in vault");
                IOUState inputState = states.get(0).getState().getData();


                progressTracker.setCurrentStep(BUILD_TRANSACTION);
                TransactionBuilder txb =
                        new TransactionBuilder(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));

                txb.addInputState(states.get(0))
                        .addCommand(new IOUContract.Commands.Retire(), Arrays.asList(inputState.getLender().getOwningKey(),
                                inputState.getBorrower().getOwningKey()));

                // This state should be initiated by borrower.
                SignedTransaction borrowerSigned = getServiceHub().signInitialTransaction(
                        txb,
                        inputState.getBorrower().getOwningKey()
                );

                progressTracker.setCurrentStep(COLLECT_SIGNATURE);
                FlowSession lenderSession = initiateFlow(getOurIdentity()); // same node
                TransactionSignature lenderSignature = subFlow(new CollectSignatureFlow(
                        borrowerSigned,
                        lenderSession,
                        inputState.getLender().getOwningKey()
                )).get(0);

                SignedTransaction fullySigned = borrowerSigned.withAdditionalSignature(lenderSignature);


                progressTracker.setCurrentStep(FINALISATION);
                return subFlow(new FinalityFlow(fullySigned,
                        Arrays.asList(),
                        progressTracker.getCurrentStep().childProgressTracker()));

            }catch (Exception e){
                throw new FlowException(e.getMessage());
            }
        }

        @InitiatedBy(Initiator.class)
        public static class Responder extends FlowLogic<SignedTransaction> {
            private final FlowSession session;

            public Responder(FlowSession session){
                this.session = session;
            }

            @Override
            @Suspendable
            public SignedTransaction call() throws FlowException {
                subFlow(new SignTransactionFlow(session) {
                    @Override
                    protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {
                        // Customized logic to verify a transaction
                    }
                });

                return subFlow(new ReceiveFinalityFlow(session));
            }
        }
    }

}
