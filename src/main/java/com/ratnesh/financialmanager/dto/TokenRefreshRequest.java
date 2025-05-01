package com.ratnesh.financialmanager.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh Token ID is required")
    String id;

    public TokenRefreshRequest(UUID uuid) {
        id = uuid.toString();
    }
}
