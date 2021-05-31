package services;

import net.corda.core.crypto.SecureHash;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.CordaService;
import net.corda.core.serialization.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CordaService
@CordaSerializable
public class ObserverService extends SingletonSerializeAsToken {
    private ServiceHub serviceHub;
    private List<PendingTransaction> list = new ArrayList<>();

    public ObserverService(ServiceHub serviceHub){
        this.serviceHub = serviceHub;
    }

    public void newPendingTransaction(PendingTransaction transaction){
        list.add(transaction);
    }

    public void giveOpinion(SecureHash transactionId, boolean approved){
        PendingTransaction transaction = null;

        for (PendingTransaction t : list){
            if (t.getTransactionId().equals(transactionId)) {
                transaction = t;
                break;
            }
        }

        if (transaction == null) throw new IllegalArgumentException("Pending Transaction not found: " + transactionId.toString());

        transaction.setApproved(approved);
        list.remove(transaction);
    }
    
    public List<PendingTransaction> getPendingTransactionsFor(String name){
        List<PendingTransaction> result = new ArrayList<>();
        
        for (PendingTransaction t : list){
            if (t.getForWhom().equals(name)){
                result.add(t);
            }
        }
        
        return result;
    }
}
