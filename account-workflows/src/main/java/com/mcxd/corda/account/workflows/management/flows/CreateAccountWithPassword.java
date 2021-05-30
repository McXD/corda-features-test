package com.mcxd.corda.account.workflows.management.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.hash.Hashing;
import com.mcxd.corda.account.workflows.management.utils.AccountWithPasswordSchemaV1;
import com.mcxd.corda.account.workflows.management.utils.Hash;
import com.r3.corda.lib.accounts.workflows.flows.CreateAccount;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;

@StartableByRPC
public class CreateAccountWithPassword extends FlowLogic<String> {
    private final String name;
    private final String plainPassword;

    public CreateAccountWithPassword(String name, String plainPassword) {
        this.name = name;
        this.plainPassword = plainPassword;
    }

    @Override
    @Suspendable
    public String call() throws FlowException {
        /* First, call CreateAccount */
        try {
            subFlow(new CreateAccount(this.name));
        }catch(FlowException e){
            return "Cannot create account: " + e.getMessage();
        }

        /* Second, store the name-hashedPassword pair */
        getServiceHub().withEntityManager(entityManager -> {
            entityManager.persist(new AccountWithPasswordSchemaV1.AccountWithPassword(this.name,
                    Hash.sha256(this.plainPassword)));
        });

        return "Account " + this.name + " is created";
    }

}
