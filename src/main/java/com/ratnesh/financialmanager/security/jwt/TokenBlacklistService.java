package com.ratnesh.financialmanager.security.jwt;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklisted:";

    TokenBlacklistService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String jti, long expirationInSeconds) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jti, true, expirationInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String jti) {
        return Optional.ofNullable((Boolean) redisTemplate.opsForValue().get(BLACKLIST_PREFIX + jti))
                        .orElse(false);
    }
    
}
