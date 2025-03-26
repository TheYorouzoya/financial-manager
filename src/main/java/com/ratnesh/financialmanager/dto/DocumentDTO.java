package com.ratnesh.financialmanager.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

// Document DTO
@Getter
@Setter
@Builder
public class DocumentDTO {
    private UUID id;

    @NotEmpty(message = "Document title is required.")
    private String title;

    private String description;
    
    @NotEmpty(message = "Document type is required.")
    private String type;

    private String filePath;
    private String originalFilename;
    private String fileType;
    private String fileSize;
    private LocalDateTime expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @NotEmpty(message = "Family ID is required.")
    private UUID familyId; // Include family ID instead of full object
    
    @NotEmpty(message = "User ID is required.")
    private UUID userId;
}
