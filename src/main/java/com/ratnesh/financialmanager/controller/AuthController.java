package com.ratnesh.financialmanager.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ratnesh.financialmanager.dto.LoginRequest;
import com.ratnesh.financialmanager.dto.TokenRefreshRequest;
import com.ratnesh.financialmanager.exceptions.RefreshTokenException;
import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.security.userdetails.CustomUserDetails;
import com.ratnesh.financialmanager.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(userDetails.getUsername(), userDetails.getId(), userDetails.getAuthorities());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken.getId().toString());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.getTokenById(request.id());
        if (refreshToken == null) {
            throw new RefreshTokenException("Refresh Token not found");
        }

        refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getUsername(), user.getId(), user.getAuthorities());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken.getId().toString());

        return ResponseEntity.ok(response);
    }
}
