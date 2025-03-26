package com.ratnesh.financialmanager.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class AlertDTO {
    private UUID id;

    @NotEmpty(message = "Alert message is required.")
    private String message;

    @NotBlank(message = "Alert type is required.")
    private String type;

    @NotEmpty(message = "Altery time is requried.")
    private LocalDateTime triggerTime;

    private boolean seen;

    @NotBlank(message = "Alter user is required.")
    private UUID userId; // Linking only the user ID
}
