package com.ratnesh.financialmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratnesh.financialmanager.dto.user.UserDTO;
import com.ratnesh.financialmanager.dto.user.UserRegistrationDTO;
import com.ratnesh.financialmanager.dto.user.UserResponseDTO;
import com.ratnesh.financialmanager.security.constants.SecurityConstants;
import com.ratnesh.financialmanager.security.jwt.JwtService;
import com.ratnesh.financialmanager.security.jwt.TokenBlacklistService;
import com.ratnesh.financialmanager.service.UserService;
import com.ratnesh.financialmanager.testConfig.WithMockCustomUser;


@WebMvcTest(UserController.class)
@EnableMethodSecurity
class UserControllerTest {
    
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
    private final static String PRINCIPAL_UUID_STRING = "2e7ce172-9cf4-448e-ae3f-6294e544e88d";
    private final static String USER_UUID_STRING = "fe399e0b-41df-47fd-bcf3-436877f3575b";
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    private final static String BASE_URL = "/api/v1/users";

    // Setup mock user data for testing
    @BeforeEach
    void setUp() {
        email = "john.doe@example.com";
        firstName = "John";
        lastName = "Doe";
        username = "johndoe";
        password = "onepassword";
        userId = UUID.fromString(USER_UUID_STRING);

        userDTO = new UserDTO(userId, email, firstName, lastName);
        userDTO.setUsername(username);
        userDTO.setPassword(password);

        userRegistrationDTO = new UserRegistrationDTO(email, firstName, lastName);
        userRegistrationDTO.setUsername(username);
        userRegistrationDTO.setPassword(password);

        userResponseDTO = new UserResponseDTO(email, firstName, lastName);
        userResponseDTO.setUsername(username);
    }


    @Nested
    class GetAllUsersTests {

        @Test
        @WithMockUser(roles = SecurityConstants.ROLE_SITE_ADMIN)
        void getAllUsers_AdminRole_ShouldReturnOkAndListOfUsers() throws Exception {
            List<UserDTO> users = Collections.singletonList(userDTO);
            when(userService.getAllUsers()).thenReturn(users);

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(userId.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(firstName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(lastName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(email));
            
            verify(userService, times(1)).getAllUsers();
        }

        @Test
        @WithMockCustomUser(roles = "USER", id = PRINCIPAL_UUID_STRING)
        void getAllUsers_UserRole_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());

            verify(userService, never()).getAllUsers();
        }
    }

    
    @Nested
    class GetUserByIdTests {
        @Test
        @WithMockCustomUser(roles = SecurityConstants.ROLE_SITE_ADMIN, id = PRINCIPAL_UUID_STRING)
        void getUserById_AdminRole_ExistingUser_ShouldReturnOkAndUser() throws Exception {
            when(userService.getUserById(userId)).thenReturn(Optional.of(userDTO));

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId.toString()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(firstName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(lastName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));

            verify(userService, times(1)).getUserById(userId);
        }

        @Test
        @WithMockUser(roles = SecurityConstants.ROLE_SITE_ADMIN)
        void getUserById_AdminRole_NonExistingUser_ShouldReturnNotFound() throws Exception {
            UUID missingId = UUID.randomUUID();
            when(userService.getUserById(missingId)).thenReturn(Optional.empty());

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", missingId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            verify(userService, times(1)).getUserById(missingId);
        }

        @Test
        @WithMockUser(roles = "USER")
        void getUserById_UserRole_ExistingUser_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());

            verify(userService, never()).getUserById(userId);
        }
    }

    
    @Nested
    class UpdateUserTests {
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = SecurityConstants.ROLE_SITE_ADMIN)
        void updateUser_AdminRole_ExistingUser_ShouldReturnOkAndUpdatedUser() throws Exception {
            String updatedEmail = "updated.email@example.com";
            String updatedFirstName = "Updated";
            String updatedLastName = "Name";
    
            UserDTO updatedUserDTO = new UserDTO(userId, updatedEmail, updatedFirstName, updatedLastName);
            updatedUserDTO.setPassword("newpassword");
    
            UserResponseDTO updatedUserResponseDTO = new UserResponseDTO(updatedEmail, updatedFirstName, updatedLastName);
            
            when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(Optional.of(updatedUserResponseDTO));
    
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", userId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUserDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedFirstName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedLastName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmail));
            
            verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
        }
    
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = SecurityConstants.ROLE_SITE_ADMIN)
        void updateUser_AdminRole_NonExistingUser_ShouldReturnNotFound() throws Exception {
            UserDTO updatedUserDTO = new UserDTO(UUID.randomUUID(), "udpated.email@example.com", "Updated", "Name");
            when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(Optional.empty());
    
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", userId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUserDTO)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
    
            verify(userService, times(1)).updateUser(eq(userId), any(UserDTO.class));
        }
    
        @Test
        @WithMockCustomUser(id = USER_UUID_STRING, roles = "USER")
        void updateUser_UserRole_SameIdAsPrincipal_ShouldReturnOkAndUpdateUser() throws Exception {
            UUID principalId = UUID.fromString(USER_UUID_STRING);
            String updatedEmail = "updated.email@example.com";
            String updatedFirstName = "Updated";
            String updatedLastName = "Name";
            UserDTO updatedUserDTO = new UserDTO(principalId, updatedEmail, updatedFirstName, updatedLastName);
            UserResponseDTO updatedUserResponseDTO = new UserResponseDTO(updatedEmail, updatedFirstName, updatedLastName);
    
            when(userService.updateUser(eq(principalId), any(UserDTO.class))).thenReturn(Optional.of(updatedUserResponseDTO));
    
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", principalId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUserDTO)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmail))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedFirstName))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedLastName));
    
            verify(userService, times(1)).updateUser(eq(principalId), any(UserDTO.class));
        }
    
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = "USER")
        void updateUser_UserRole_DifferentIdThanPrincipal_ShouldReturnForbidden() throws Exception {
            UserDTO updatedUserDTO = new UserDTO(userId, "updated.email@example.com", "Updated", "Name");
    
            mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", userId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedUserDTO)))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
    
            verify(userService, never()).updateUser(eq(userId), any(UserDTO.class));
        }    
    }


    @Nested
    class DeleteUserTests {
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = SecurityConstants.ROLE_SITE_ADMIN)
        void deleteUser_AdminRole_ExistingUser_ShouldReturnNoContent() throws Exception {
            doNothing().when(userService).deleteUser(userId);
    
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", userId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
    
            verify(userService, times(1)).deleteUser(userId);
        }
    
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = SecurityConstants.ROLE_SITE_ADMIN)
        void deleteUser_AdminRole_NonExistingUser_ShouldReturnNoContent() throws Exception {
            UUID randomUUID = UUID.randomUUID();
            doNothing().when(userService).deleteUser(randomUUID);
    
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", randomUUID).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());
    
            verify(userService, times(1)).deleteUser(randomUUID);
        }
    
        @Test
        @WithMockCustomUser(id = PRINCIPAL_UUID_STRING, roles = "USER")
        void deleteUser_UserRole_ShouldReturnForbidden() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", userId).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
    
            verify(userService, never()).deleteUser(userId);
        }
    }
}
