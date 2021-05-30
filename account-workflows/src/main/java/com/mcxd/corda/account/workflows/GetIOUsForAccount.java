package com.mcxd.corda.account.workflows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.account.states.IOUState;
import com.mcxd.corda.account.workflows.model.IOU;
import com.r3.corda.lib.accounts.workflows.flows.AccountInfoByKey;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.AbstractParty;

import java.util.ArrayList;
import java.util.List;

@StartableByRPC
public class GetIOUsForAccount extends FlowLogic<List<IOU>> {
    private final String username;

    public GetIOUsForAccount(String username) {
        this.username = username;
    }

    @Override
    @Suspendable
    public List<IOU> call() throws FlowException {
        List<StateAndRef<IOUState>> states = getServiceHub().getVaultService().queryBy(IOUState.class).getStates();
        List<IOU> result = new ArrayList<>();

        for (StateAndRef<IOUState> stateStateAndRef : states){
            AbstractParty lender = stateStateAndRef.getState().getData().getLender();
            AbstractParty borrower = stateStateAndRef.getState().getData().getBorrower();

            String lenderName = subFlow(new AccountInfoByKey(lender.getOwningKey())).getState().getData().getName();
            String borrowerName = subFlow(new AccountInfoByKey(borrower.getOwningKey())).getState().getData().getName();

            if (lenderName.equals(this.username) || borrowerName.equals(this.username)) result.add(
                    new IOU(
                            lenderName,
                            borrowerName,
                            stateStateAndRef.getState().getData().getAmount(),
                            stateStateAndRef.getState().getData().getLinearId().toString()
                    )
            );

        }

        return result;
    }
}