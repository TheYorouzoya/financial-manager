package com.ratnesh.financialmanager.security.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
    
    private final Map<String, Object> attributes;
    private final String name;
    private final UUID id;
    private final Collection<GrantedAuthority> authorities;

    public CustomOAuth2User(String name, UUID id, Map<String, Object> attributes, Collection<GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getId() {
        return id;
    }

}
