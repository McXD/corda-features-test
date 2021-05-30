package com.mcxd.corda.account.workflows.model;

import net.corda.core.serialization.CordaSerializable;

@CordaSerializable
public class IOU {
    private String lender;
    private String borrower;
    private long amount;
    private String uid;

    public IOU(String lender, String borrower, long amount, String uid) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.uid = uid;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
