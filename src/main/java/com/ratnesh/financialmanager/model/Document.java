package com.ratnesh.financialmanager.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

// Document Entity
@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
   
    @Id
    @UuidGenerator
    private UUID id;
    
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private String filePath;
    private String originalFilename;
    private String fileType;
    private Long fileSize;
    private LocalDateTime expiryDate;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
