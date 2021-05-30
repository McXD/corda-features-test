package com.mcxd.corda.account.client.model;

public class IOUDetail {
    private String borrower;
    private long amount;

    public IOUDetail(){}

    public IOUDetail(String borrower, long amount) {
        this.borrower = borrower;
        this.amount = amount;
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
}
