package com.ratnesh.financialmanager.testConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.ratnesh.financialmanager.security.constants.SecurityConstants;
import com.ratnesh.financialmanager.security.userdetails.CustomUserDetails;


final class WithUserDetailsSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        UUID userId = annotation.id().isEmpty() ? UUID.randomUUID() : UUID.fromString(annotation.id());
        
        String annotationRole = "ROLE_" + annotation.roles()[0];
        SimpleGrantedAuthority role = new SimpleGrantedAuthority(annotationRole);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(role);
        authorities.addAll(
            SecurityConstants.getRolePrivilegeMapping().get(annotationRole)
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege))
                .collect(Collectors.toList())
        );

        CustomUserDetails principal = new CustomUserDetails(userId, annotation.username(), "password", authorities);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal, principal.getPassword(), principal.getAuthorities());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
    }
}
