package com.mcxd.corda.account.workflows.management.utils;

import net.corda.core.schemas.MappedSchema;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;

public class AccountWithPasswordSchemaV1 extends MappedSchema {

    public AccountWithPasswordSchemaV1() {
        super(AccountWithPasswordSchema.class, 1, Arrays.asList(AccountWithPassword.class));
    }

    @Entity
    @Table(name="account_password")
    public static class AccountWithPassword{
        @Id
        private String name;
        private String hashedPassword;

        public AccountWithPassword(){}

        public AccountWithPassword(String name, String hashedPassword) {
            this.name = name;
            this.hashedPassword = hashedPassword;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public void setHashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
        }
    }
}
