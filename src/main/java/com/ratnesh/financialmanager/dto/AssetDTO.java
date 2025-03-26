package com.ratnesh.financialmanager.dto;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.ratnesh.financialmanager.model.AssetType;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Getter
@Setter
@Builder
public class AssetDTO {
    private UUID id;

    @NotEmpty(message = "Asset name is required.")
    private String name;

    private String description;

    @NotEmpty(message = "Asset type is required.")
    private AssetType type;

    private BigDecimal currentValue;

    @NotEmpty(message = "Asset purchase value is required.")
    private BigDecimal purchaseValue;

    @NotEmpty(message = "Asset acquisition date is requried.")
    private LocalDate acquisitionDate;

    private String location;

    @NotEmpty(message = "Family ID is required.")
    private UUID familyId;  // Include family ID instead of full object

    @NotEmpty(message = "User ID is required.")
    private UUID userId;    // Include user ID instead of full object
}