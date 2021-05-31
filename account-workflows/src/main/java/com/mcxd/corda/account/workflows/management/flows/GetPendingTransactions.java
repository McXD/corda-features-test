package com.mcxd.corda.account.workflows.management.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import services.ObserverService;
import services.PendingTransaction;

import java.util.List;

@StartableByRPC
public class GetPendingTransactions extends FlowLogic<List<PendingTransaction>> {
    private final String name;

    public GetPendingTransactions(String name) {
        this.name = name;
    }

    @Override
    @Suspendable
    public List<PendingTransaction> call() throws FlowException {
        return getServiceHub().cordaService(ObserverService.class).getPendingTransactionsFor(this.name);
    }
}
