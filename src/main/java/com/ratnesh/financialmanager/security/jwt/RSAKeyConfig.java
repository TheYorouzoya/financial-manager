package com.ratnesh.financialmanager.security.jwt;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RSAKeyConfig {
    
    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    public RSAKeyConfig() {
        KeyPair pair = KeyGenerator.generateRSAKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }
}
