package com.ratnesh.financialmanager;

import java.util.Collections;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


final class WithUserDetailsSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        UUID userId = annotation.id().isEmpty() ? UUID.randomUUID() : UUID.fromString(annotation.id());
        
        UserDetails principal = new User(annotation.username(), "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + annotation.roles()[0]))) {
                private final UUID id = userId;
                public UUID getId() {
                    return this.id;
                }
            };
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
    }
}
