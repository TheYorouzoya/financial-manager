package com.ratnesh.financialmanager.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratnesh.financialmanager.dto.LoginRequest;
import com.ratnesh.financialmanager.dto.TokenRefreshRequest;
import com.ratnesh.financialmanager.dto.user.UserRegistrationDTO;
import com.ratnesh.financialmanager.dto.user.UserResponseDTO;
import com.ratnesh.financialmanager.exceptions.RefreshTokenException;
import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.security.jwt.TokenBlacklistService;
import com.ratnesh.financialmanager.security.userdetails.CustomUserDetails;
import com.ratnesh.financialmanager.service.RefreshTokenService;
import com.ratnesh.financialmanager.service.UserService;
import com.ratnesh.financialmanager.testConfig.WithMockCustomUser;


@WebMvcTest(AuthController.class)
@EnableMethodSecurity
public class AuthControllerTest {
    
     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // mock necessary dependencies

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;
    
    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private User user;

    private LoginRequest loginRequest;
    private CustomUserDetails userDetails;
    private UUID userId;
    private String accessToken;
    private RefreshToken refreshToken;
    private TokenRefreshRequest tokenRefreshRequest;
    private UserRegistrationDTO registrationDTO;
    private UserResponseDTO responseDTO;
    private Jwt jwt;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    private final static String BASE_URL = "/api/v1/auth";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        username = "testUser";
        password = "password";
        email = "test@example.com";
        firstName = "Test";
        lastName = "User";

        user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(new HashSet<Role>());

        loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        userDetails = new CustomUserDetails(
            userId, 
            username, 
            password, 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        accessToken = "mockAccessToken";
        refreshToken = new RefreshToken();
        refreshToken.setId(UUID.randomUUID());
        refreshToken.setUser(user);
        tokenRefreshRequest = new TokenRefreshRequest(refreshToken.getId());
        registrationDTO = new UserRegistrationDTO(email, firstName, lastName);
        registrationDTO.setUsername(username);
        registrationDTO.setPassword(password);
        responseDTO = new UserResponseDTO(email, firstName, lastName);

        jwt = Jwt.withTokenValue("dummyJwt")
                    .header("alg", "HS256")
                    .claim("sub", username)
                    .claim("id", userId.toString())
                    .claim("roles", List.of("ROLE_USER"))
                    .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                    .issuedAt(Instant.now())
                    .build();
    }

    
    @Nested
    class LoginTests {

        @Test
        void login_ValidCredentials_ShouldReturnOkAndTokenResponse() throws Exception {
            Authentication authenticationResult = new UsernamePasswordAuthenticationToken(
                userDetails, 
                password, 
                userDetails.getAuthorities()
            );
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationResult);
            when(jwtService.generateToken(eq(username), eq(userId), any()))
                .thenReturn(accessToken);
            when(refreshTokenService.createRefreshToken(userId))
                .thenReturn(refreshToken);

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/login")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken")
                        .value(accessToken))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken")
                        .value(refreshToken.getId().toString()));
                
            verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, times(1))
                .generateToken(eq(username), eq(userId), any());
            verify(refreshTokenService, times(1))
                .createRefreshToken(userId);
        }

        @Test
        void login_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid Credentials"));

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/login")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());

            verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
            verifyNoInteractions(jwtService);
            verifyNoInteractions(refreshTokenService);
        }

        @Test
        void login_MissingCredentials_ShouldReturnBadRequest() throws Exception {
            LoginRequest invalidRequest = new LoginRequest();

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/login")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            
            verifyNoInteractions(authenticationManager);
            verifyNoInteractions(jwtService);
            verifyNoInteractions(refreshTokenService);
        }
    }

    @Nested
    class RefreshTests {

        @Test
        void refreshToken_ValidToken_ShouldReturnOkAndNewTokenResponse() throws Exception {
            when(refreshTokenService.getTokenById(refreshToken.getId())).thenReturn(refreshToken);
            when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);
            when(jwtService.generateToken(eq(username), eq(userId), any())).thenReturn(accessToken);

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/refresh")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tokenRefreshRequest)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(accessToken))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value(refreshToken.getId().toString()));

            verify(refreshTokenService, times(1)).getTokenById(refreshToken.getId());
            verify(refreshTokenService, times(1)).verifyExpiration(refreshToken);
            verify(jwtService, times(1)).generateToken(eq(username), eq(userId), any());
        }

        @Test
        void refreshToken_InvalidTokenId_ShouldReturnUnauthorized() throws Exception {
            when(refreshTokenService.getTokenById(any())).thenReturn(null);
            
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/refresh")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new TokenRefreshRequest(userId))))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Refresh Token not found"));

            verify(refreshTokenService, times(1)).getTokenById(any());
            verifyNoInteractions(jwtService);
        }

        @Test
        void refreshToken_ExpiredToken_ShouldReturnUnauthrorized() throws Exception {
            RefreshToken expiredToken = new RefreshToken();
            expiredToken.setId(UUID.randomUUID());
            expiredToken.setExpiresAt(Instant.now().minus(Duration.ofMinutes(5)));

            when(refreshTokenService.getTokenById(expiredToken.getId())).thenReturn(expiredToken);
            when(refreshTokenService.verifyExpiration(expiredToken)).thenThrow(new RefreshTokenException("Refresh Token expired. Please log in again."));
            
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/refresh")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new TokenRefreshRequest(expiredToken.getId()))))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Refresh Token expired. Please log in again."));

            verify(refreshTokenService, times(1)).getTokenById(any());
            verify(refreshTokenService, times(1)).verifyExpiration(any(RefreshToken.class));
            verifyNoInteractions(jwtService);
        }
    }

    @Nested
    class LogoutTests {

        @Test
        @WithMockCustomUser(roles = "USER")
        void logout_ValidJwt_ShouldBlackListTokenAndRevokeRefreshToken() throws Exception {
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/logout")
                            .with(csrf())
                            .with(oauth2Login())
                            .with(jwt().jwt(jwt))
                            .contentType(MediaType.APPLICATION_JSON)
                            .principal(authentication))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            verify(tokenBlacklistService, times(1)).blacklistToken(eq(jwt.getId()), anyLong());
            verify(refreshTokenService, times(1)).deleteAllUserRefreshTokens(userId);
        }
    }

    @Nested
    class RegisterTests {

        @Test
        void registerUser_ValidInput_ShouldReturnCreatedAndUserResponse() throws Exception {
            when(userService.createUser(any(UserRegistrationDTO.class))).thenReturn(responseDTO);

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/register")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registrationDTO)))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName));

            verify(userService, times(1)).createUser(any(UserRegistrationDTO.class));
        }

        @Test
        void registerUser_InvalidInput_ShouldReturnBadRequest() throws Exception {
            UserRegistrationDTO invalidDTO = new UserRegistrationDTO("", "Invalid", "DTO");

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/register")
                            .with(csrf())
                            .with(oauth2Login())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            verify(userService, never()).createUser(any());
        }
    }
}
