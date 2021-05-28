package com.mcxd.corda.account.contracts;

import com.mcxd.corda.account.states.IOUState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IOUContract implements Contract {
    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        // extract command
        List<CommandWithParties<CommandData>> commandWithParties = tx.getCommands();
        if (commandWithParties.size() != 1) throw new IllegalArgumentException("Require exactly one command, given " + commandWithParties.size());
        CommandWithParties<CommandData> singleCommandWithParties = commandWithParties.get(0);
        CommandData command = singleCommandWithParties.getValue();

        // Command Switch
        if (command instanceof Commands.Create){
            // shape
            if (tx.getInputStates().size() != 0) throw new IllegalArgumentException("Require 0 input state, given " + tx.getInputStates().size());
            if (tx.getOutputStates().size() != 1) throw new IllegalArgumentException("Require 1 output state, given " + tx.getOutputStates().size());

            // Data
            IOUState outputState = (IOUState) tx.getOutput(0);

                // parties
            if (outputState.getBorrower().equals(outputState.getLender())) throw new IllegalArgumentException("Cannot" +
                    " lend to oneself");

                // amount
            if (outputState.getAmount() <= 0) throw new IllegalArgumentException("Cannot issue non-positive IOU");

            //Signing
            if (! singleCommandWithParties.getSigners().contains(outputState.getBorrower().getOwningKey())) throw new IllegalArgumentException("Borrower must sign");
            if (! singleCommandWithParties.getSigners().contains(outputState.getLender().getOwningKey())) throw new IllegalArgumentException("Lender must sign");

        }else if (command instanceof Commands.Pay){
            // shape
            if (tx.getInputStates().size() != 1) throw new IllegalArgumentException("Require 1 input state, given " + tx.getInputStates().size());
            if (tx.getOutputStates().size() != 1) throw new IllegalArgumentException("Require 1 output state, given " + tx.getOutputStates().size());

            // Data
            IOUState inputState = (IOUState) tx.getInput(0);
            IOUState outputState = (IOUState) tx.getOutput(0);
            
                // the same linear state
            if (! inputState.getLinearId().equals(outputState.getLinearId())) throw new IllegalArgumentException(
                    "Input state and Output state must be in the same linear state");
                // must 'pay'
            if (! (inputState.getAmount() > outputState.getAmount())) throw new IllegalArgumentException("Amount in " +
                    "the Output state must be smaller than that in the Input state");
                // cannot overpay
            if (outputState.getAmount() < 0) throw new IllegalArgumentException("Amount in the Output state cannot be" +
                    " negative (overpayment is not allowed)");
                // only amount is allowed to change in linear transition
            if (! inputState.getLender().equals(outputState.getLender())) throw new IllegalArgumentException("Lender " +
                    "cannot change in a linear transition");
            if (! inputState.getBorrower().equals(outputState.getBorrower())) throw new IllegalArgumentException(
                    "Borrower cannot change in a linear transition");

            // Signing
            if (! singleCommandWithParties.getSigners().contains(outputState.getBorrower().getOwningKey())) throw new IllegalArgumentException("Borrower must sign");
            if (! singleCommandWithParties.getSigners().contains(outputState.getLender().getOwningKey())) throw new IllegalArgumentException("Lender must sign");


        } else if (command instanceof Commands.Retire){
            // shape
            if (tx.getInputStates().size() != 1) throw new IllegalArgumentException("Require 1 input state, given " + tx.getInputStates().size());
            if (tx.getOutputStates().size() != 0) throw new IllegalArgumentException("Require 0 output state, given " + tx.getOutputStates().size());

            // Data
            IOUState inputState = (IOUState) tx.getInput(0);
            if (inputState.getAmount() != 0) throw new IllegalArgumentException("IOU can only be retired when amount " +
                    "is 0");


            //Signing
            if (! singleCommandWithParties.getSigners().contains(inputState.getBorrower().getOwningKey())) throw new IllegalArgumentException("Borrower must sign");
            if (! singleCommandWithParties.getSigners().contains(inputState.getLender().getOwningKey())) throw new IllegalArgumentException("Lender must sign");

        }else{
            throw new IllegalArgumentException("Command " + command.getClass().getName() + " not recognized");
        }
    }

    public interface Commands extends CommandData{
        class Create implements Commands{}
        class Retire implements Commands{}
        class Pay implements Commands{}
    }
}
