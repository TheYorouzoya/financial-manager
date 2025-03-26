package com.ratnesh.financialmanager.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;
    
    @Column(nullable = false)
    private LocalDateTime triggerTime;
    
    private boolean seen;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public enum AlertType {
        DOCUMENT_EXPIRATION, TRANSACTION_REMINDER, GENERAL_NOTIFICATION
    }
}