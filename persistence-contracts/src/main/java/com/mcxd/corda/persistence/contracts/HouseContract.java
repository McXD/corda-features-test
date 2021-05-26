package com.mcxd.corda.persistence.contracts;

import com.mcxd.corda.persistence.states.HouseState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

public class HouseContract implements Contract {
    public static String id = "com.mcxd.corda.persistence.contracts.HouseContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommands().size() != 1) throw new IllegalArgumentException("Require exactly one command, given " + tx.getCommands().size());

        CommandData commandData = tx.getCommand(0).getValue();
        if (commandData instanceof Commands.Create){
            if (tx.getInputStates().size() != 0) throw new IllegalArgumentException("Require exactly 0 input states, given " + tx.getInputStates().size());

            if (tx.getOutputStates().size() != 1) throw new IllegalArgumentException("Require exactly 1 output states, given " + tx.getOutputStates().size());

            HouseState houseState = (HouseState) tx.getOutputStates().get(0);
            if (houseState.getHolder() == null) throw new IllegalArgumentException("House must have a holder");
            if (houseState.getPrice() < 0) throw new IllegalArgumentException("Price of the house must be positive, provided " + houseState.getPrice());

            if (! (tx.getCommands().get(0).getSigners().get(0).equals(houseState.getHolder().getOwningKey()))) throw new IllegalArgumentException("Holder of the house must sign.");
        }else{
            throw new IllegalArgumentException("Only support HouseContract.Commands.Create, given " + commandData.getClass().getName());
        }
    }

    public interface Commands extends CommandData{
        class Create implements Commands{}
    }
}
