package com.ratnesh.financialmanager.security.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.security.jwt.expiration-ms}")
    long expiration;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(String subject, UUID id, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofMillis(expiration)))
                .subject(subject)
                .id(UUID.randomUUID().toString())
                .claim("id", id)
                .claim("scope", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return token;
    }

    public Jwt getJwtFromTokenString(String token) throws JwtException {
        return jwtDecoder.decode(token);
    }
}
