package com.mcxd.corda.account.client.model;

public class IOUDeleteDetail {
    private String uid;

    public IOUDeleteDetail(){}

    public IOUDeleteDetail(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
