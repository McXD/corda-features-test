package com.mcxd.corda.account.workflows.management.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.account.workflows.management.utils.AccountWithPasswordSchemaV1;
import com.r3.corda.lib.accounts.workflows.flows.AccountInfoByName;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

import java.util.concurrent.atomic.AtomicReference;

@StartableByRPC
public class GetAccount extends FlowLogic<String> {
    private String username;

    public GetAccount(String username) {
        this.username = username;
    }


    @Override
    @Suspendable
    public String call() throws FlowException {
        try {
            if (subFlow(new AccountInfoByName(this.username)).isEmpty())
                throw new FlowException("Account: " + this.username +
                        " not " +
                        "found");

            AtomicReference<String> hashedPassword = new AtomicReference<>();

            getServiceHub().withEntityManager(entityManager -> {
                AccountWithPasswordSchemaV1.AccountWithPassword account =
                        entityManager.find(AccountWithPasswordSchemaV1.AccountWithPassword.class,
                                this.username);

                hashedPassword.set(account.getHashedPassword());
            });

            return hashedPassword.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FlowException(e.getMessage());
        }
    }
}
