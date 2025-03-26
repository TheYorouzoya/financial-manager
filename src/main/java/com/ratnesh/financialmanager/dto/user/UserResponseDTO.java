package com.ratnesh.financialmanager.dto.user;

import lombok.Data;

// Filtered API response for User Entity
@Data
public class UserResponseDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
}
