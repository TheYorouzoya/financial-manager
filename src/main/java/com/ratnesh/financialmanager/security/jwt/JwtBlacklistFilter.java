package com.ratnesh.financialmanager.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ratnesh.financialmanager.exceptions.TokenBlacklistedException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String jti = jwtService.getJwtFromTokenString(token).getId();

                if (blacklistService.isTokenBlacklisted(jti)) {
                    throw new TokenBlacklistedException("Token has been blacklisted.");
                }
            } catch (TokenBlacklistedException | IllegalArgumentException ex) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException(ex.getMessage(), ex));
                return;
            } catch (JwtException ex) {
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("Malformed or Invalid bearer token."));
                return;
            } catch (AuthenticationException ex) {
                authenticationEntryPoint.commence(request, response, ex);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
