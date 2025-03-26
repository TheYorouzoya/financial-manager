package com.ratnesh.financialmanager.dto.user;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.ratnesh.financialmanager.model.Role;

import lombok.Data;

// Full User Entity mapping
@Data
public class UserDTO {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Role> roles;
    private UUID familyId; 
}