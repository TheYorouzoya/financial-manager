package com.ratnesh.financialmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Transaction DTO
@Getter
@Setter
@Builder
public class TransactionDTO {
    private UUID id;

    @NotEmpty(message = "Transaction amount is required.")
    private BigDecimal amount;

    private String description;

    @NotEmpty(message = "Transaction category is required.")
    private String category;

    @NotEmpty(message = "Transaction type is required.")
    private String type;

    @NotEmpty(message = "Transaction date is required.")
    private LocalDateTime transactionDate;
    private boolean flagged;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotEmpty(message = "Transaction account ID is required.")
    private UUID accountId; // Include account ID instead of full object
}