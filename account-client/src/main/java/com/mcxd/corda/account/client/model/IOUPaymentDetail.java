package com.mcxd.corda.account.client.model;

public class IOUPaymentDetail {
    private String uid;
    private long payment;

    public IOUPaymentDetail() {}

    public IOUPaymentDetail(String uid, long payment) {
        this.uid = uid;
        this.payment = payment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPayment() {
        return payment;
    }

    public void setPayment(long payment) {
        this.payment = payment;
    }
}
