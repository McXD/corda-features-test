package com.mcxd.corda.account.client.security;

import com.mcxd.corda.account.client.utils.NodeRPCConnection;
import com.mcxd.corda.account.workflows.management.flows.GetAccount;
import net.corda.core.messaging.CordaRPCOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class IOUUserService implements UserDetailsService {

    @Autowired
    private CordaRPCOps cordaProxy;

    public IOUUserService(NodeRPCConnection nodeRPCConnection){
        this.cordaProxy = nodeRPCConnection.getProxy();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String password = cordaProxy.startFlowDynamic(GetAccount.class, username).getReturnValue().get();

            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return Collections.singleton(new GrantedAuthority() {
                        @Override
                        public String getAuthority() {
                            return "ROLE_USER";
                        }
                    });
                }

                @Override
                public String getPassword() {
                    return password;
                }

                @Override
                public String getUsername() {
                    return username;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
    }
}
