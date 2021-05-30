package com.mcxd.corda.account.workflows.management.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Hash {
    public static String sha256(String message){
        return Hashing.sha256().hashString(
                message,
                StandardCharsets.UTF_8
        ).toString();
    }

    public static void main(String... args){
        System.out.println(sha256("message"));
    }
}
