package com.ratnesh.financialmanager.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


// Account DTO
@Data
public class AccountDTO {
    private UUID id;

    @NotEmpty(message = "Account name is required.")
    private String name;

    @NotEmpty(message = "Account Number is required.")
    private String accountNumber;

    @Size(max = 1000, message = "Description must be less than 1000 characters.")
    private String description;

    @NotNull(message = "Account type is required.")
    private String type;

    private BigDecimal balance;

    @NotEmpty(message = "Account institution is requried.")
    private String institution;

    @NotEmpty(message = "Account user is required.")
    private UUID userId; // Include user ID instead of full object

    @NotEmpty(message = "Family is required.")
    private UUID familyId; // Include family ID instead of full object
}