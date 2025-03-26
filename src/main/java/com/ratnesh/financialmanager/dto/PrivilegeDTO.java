package com.ratnesh.financialmanager.dto;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrivilegeDTO {
    private UUID id;

    @NotBlank(message = "Privilege name is required.")
    private String name;

    private Set<UUID> roles;
}