package com.mcxd.corda.persistence.workflows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.persistence.contracts.HouseContract;
import com.mcxd.corda.persistence.states.HouseState;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

@StartableByRPC
public class CreateHouse extends FlowLogic<SignedTransaction> {
    private final String location;
    private final double size;
    private final double price;

    public CreateHouse(String location, double size, double price){
        this.location = location;
        this.size = size;
        this.price = price;
    }

    @Override
    @Suspendable
    public SignedTransaction call() throws FlowException {
        HouseState output = new HouseState(this.location, this.price, this.size, getOurIdentity());
        TransactionBuilder txb = new TransactionBuilder(getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0));
        txb.addOutputState(output)
                .addCommand(new HouseContract.Commands.Create(), getOurIdentity().getOwningKey());
        SignedTransaction stx = getServiceHub().signInitialTransaction(txb);

        return subFlow(new FinalityFlow(stx));
    }
}
