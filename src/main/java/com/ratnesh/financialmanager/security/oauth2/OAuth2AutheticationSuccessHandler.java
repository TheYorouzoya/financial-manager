package com.ratnesh.financialmanager.security.oauth2;

import java.io.IOException;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratnesh.financialmanager.dto.TokenResponse;
import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AutheticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(oAuth2User.getName(), oAuth2User.getId(), oAuth2User.getAuthorities());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(oAuth2User.getId());

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken.getId().toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
    }
}
