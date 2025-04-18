package com.ratnesh.financialmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username cannot be blank")
    private String username;
    
    @NotBlank(message = "password cannot be blank")
    private String password;
}
