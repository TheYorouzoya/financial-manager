package com.ratnesh.financialmanager.security.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class KeyGenerator {
    public static KeyPair generateRSAKey() {
        KeyPair pair;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            pair = keyPairGenerator.generateKeyPair();

        } catch (Exception e) {
            throw new IllegalStateException();
        }

        return pair;
    }
}
