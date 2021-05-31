package com.mcxd.corda.account.client.model;

public class SignatureDetail {
    private boolean approved;

    public SignatureDetail(){}

    public SignatureDetail(boolean approved){
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
