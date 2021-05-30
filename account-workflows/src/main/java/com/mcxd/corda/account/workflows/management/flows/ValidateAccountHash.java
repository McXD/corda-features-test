package com.mcxd.corda.account.workflows.management.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.mcxd.corda.account.workflows.management.utils.AccountWithPasswordSchemaV1;
import com.r3.corda.lib.accounts.workflows.flows.AccountInfoByName;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

import java.util.concurrent.atomic.AtomicReference;

@StartableByRPC
public class ValidateAccountHash extends FlowLogic<Boolean> {
    private final String name;
    private final String hashPassword;

    public ValidateAccountHash(String name, String hashPassword) {
        this.name = name;
        this.hashPassword = hashPassword;
    }

    @Override
    @Suspendable
    public Boolean call() throws FlowException {
        if (subFlow(new AccountInfoByName(this.name)).isEmpty()) throw new FlowException("Account: " + this.name + " not " +
                "found");

        AtomicReference<Boolean> validated = new AtomicReference<>(false);

        getServiceHub().withEntityManager(entityManager -> {
            AccountWithPasswordSchemaV1.AccountWithPassword account =
                    entityManager.find(AccountWithPasswordSchemaV1.AccountWithPassword.class,
                            this.name);

            if (this.hashPassword.equals(account.getHashedPassword())) validated.set(true);

        });

        return validated.get();
    }
}
