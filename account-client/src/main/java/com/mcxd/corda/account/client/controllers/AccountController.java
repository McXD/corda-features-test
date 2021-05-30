package com.mcxd.corda.account.client.controllers;

import com.mcxd.corda.account.client.model.IOUAccount;
import com.mcxd.corda.account.client.utils.NodeRPCConnection;
import com.mcxd.corda.account.workflows.management.flows.CreateAccountWithPassword;
import net.corda.core.messaging.CordaRPCOps;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final CordaRPCOps cordaProxy;

    public AccountController(NodeRPCConnection connection){
        this.cordaProxy = connection.getProxy();
    }

    @PostMapping("/register")
    public void registerAccount(@RequestBody IOUAccount newAccount) {
        cordaProxy.startFlowDynamic(CreateAccountWithPassword.class, newAccount.getUsername(),
                newAccount.getPassword());
    }
}
