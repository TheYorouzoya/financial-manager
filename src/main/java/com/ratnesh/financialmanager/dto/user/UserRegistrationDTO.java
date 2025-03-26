package com.ratnesh.financialmanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 50, message = "Username must be between 2 and 50 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
    private String email;

    @NotEmpty(message = "Password is required.")
    @Size(min = 8, message = "Password must be more than 8 characters long.")
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureURL;
}
