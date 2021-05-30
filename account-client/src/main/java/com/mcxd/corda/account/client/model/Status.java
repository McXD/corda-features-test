package com.mcxd.corda.account.client.model;

public class Status {
    private boolean success;

    public Status(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
