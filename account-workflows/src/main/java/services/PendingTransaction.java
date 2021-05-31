package services;

import net.corda.core.crypto.SecureHash;
import net.corda.core.serialization.CordaSerializable;

import java.util.Observable;

@CordaSerializable
public class PendingTransaction{
    private SecureHash transactionId;
    private String forWhom;
    private Boolean approved;

    public PendingTransaction(SecureHash transactionId, String forWhom, Boolean approved) {
        this.transactionId = transactionId;
        this.forWhom = forWhom;
        this.approved = approved;
    }

    public SecureHash getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(SecureHash transactionId) {
        this.transactionId = transactionId;
    }

    public String getForWhom() {
        return forWhom;
    }

    public void setForWhom(String forWhom) {
        this.forWhom = forWhom;
    }

    public synchronized Boolean getApproved() throws InterruptedException {
        this.wait();
        return approved;
    }

    public synchronized void setApproved(Boolean approved) {
        this.approved = approved;
        this.notifyAll();
    }
}
