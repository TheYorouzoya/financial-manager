package com.ratnesh.financialmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratnesh.financialmanager.dto.user.UserDTO;
import com.ratnesh.financialmanager.dto.user.UserRegistrationDTO;
import com.ratnesh.financialmanager.dto.user.UserResponseDTO;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.security.jwt.TokenBlacklistService;
import com.ratnesh.financialmanager.service.UserService;


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
    private TokenBlacklistService tokenBlackListService;

    @MockitoBean
    private AuthenticationEntryPoint authenticationEntryPoint;

    private UserDTO userDTO;
    private UserRegistrationDTO userRegistrationDTO;
    private UserResponseDTO userResponseDTO;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    private final static String BASE_URL = "/api/v1/users";


    // Setup mock user data for testing
    // @BeforeEach
    // void setUp() {
    //     email = "john.doe@example.com";
    //     firstName = "John";
    //     lastName = "Doe";
    //     username = "johndoe";
    //     password = "onepassword";

    //     userId = UUID.randomUUID();
    //     userDTO = new UserDTO(userId, email, firstName, lastName);
    //     userDTO.setUsername(username);
    //     userDTO.setPassword(password);

    //     userRegistrationDTO = new UserRegistrationDTO(email, firstName, lastName);
    //     userRegistrationDTO.setUsername(username);
    //     userRegistrationDTO.setPassword(password);

    //     userResponseDTO = new UserResponseDTO(email, firstName, lastName);
    //     userResponseDTO.setUsername(username);
    // }

    // // cleanup allocated memory references
    // @AfterEach
    // void tearDown() {
    //     userDTO = null;
    //     userRegistrationDTO = null;
    //     userResponseDTO = null;
    // }

    // @Test
    // void registerUser_ValidInput_ShouldReturnCreatedAndUserResponse() throws Exception {
    //     when(userService.createUser(any(UserRegistrationDTO.class))).thenReturn(userResponseDTO);

    //     mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/register")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(userRegistrationDTO)))
    //             .andExpect(MockMvcResultMatchers.status().isCreated())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId.toString()))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));

    //     verify(userService, times(1)).createUser(any(UserRegistrationDTO.class));
    // }

    // @Test
    // void registerUser_InvalidInput_ShouldReturnBadRequest() throws Exception {
    //     UserRegistrationDTO invalidDTO = new UserRegistrationDTO();
    //     invalidDTO.setEmail("john.doe@example.com");

    //     mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/register")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(invalidDTO)))
    //             .andExpect(MockMvcResultMatchers.status().isBadRequest());

    //     verify(userService, never()).createUser(any());
    // }
}
