package com.ratnesh.financialmanager.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RSAKeyConfig {
    
    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

    public RSAKeyConfig(
        @Value("${spring.security.jwt.public-key-location}") Resource publicKeyResource,
        @Value("${spring.security.jwt.private-key-location}") Resource privateKeyResource
    ) {
        try {
            this.publicKey = loadPublicKey(publicKeyResource);
            this.privateKey = loadPrivateKey(privateKeyResource);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA key pair", e);
        }
    }

    private RSAPublicKey loadPublicKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String cleaned = key.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                            .replaceAll("-----END PUBLIC KEY-----", "")
                            .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(cleaned);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    private RSAPrivateKey loadPrivateKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String cleaned = key.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                            .replaceAll("-----END PRIVATE KEY-----", "")
                            .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(cleaned);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
    }
}
