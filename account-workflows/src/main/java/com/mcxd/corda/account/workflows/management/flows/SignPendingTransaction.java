package com.mcxd.corda.account.workflows.management.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import services.ObserverService;

@StartableByRPC
public class SignPendingTransaction extends FlowLogic<Boolean> {
    private final SecureHash transactionId;
    private final boolean approved;

    public SignPendingTransaction(SecureHash transactionId, boolean approved) {
        this.transactionId = transactionId;
        this.approved = approved;
    }

    @Override
    @Suspendable
    public Boolean call() throws FlowException {
        try{
            getServiceHub().cordaService(ObserverService.class).giveOpinion(this.transactionId, this.approved);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
