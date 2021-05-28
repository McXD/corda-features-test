package com.mcxd.corda.account.states;

import com.mcxd.corda.account.contracts.IOUContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BelongsToContract(IOUContract.class)
public class IOUState implements ContractState, LinearState {
    private final AbstractParty lender;
    private final AbstractParty borrower;
    private final long amount;
    private final UniqueIdentifier uid;

    /*
      Caveat: UniqueIdentifier must be passed to the constructor instead of silently create an instance itself.
      Because the state needs to be restored from the vault.
     */
    public IOUState(AbstractParty lender, AbstractParty borrower, long amount, UniqueIdentifier uid) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.uid = uid;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(lender, borrower);
    }

    public AbstractParty getLender() {
        return lender;
    }

    public AbstractParty getBorrower() {
        return borrower;
    }

    public long getAmount() {
        return amount;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return this.uid;
    }

    @Override
    public String toString(){
        return "lender: " + this.lender + ", borrower: " + this.borrower + ", amount: " + this.amount +
        ", uid: " + this.uid;
    }
}
