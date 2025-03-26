package com.ratnesh.financialmanager.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

// Family Entity
@Entity
@Table(name = "family")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Family {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    private String name;
    private String description;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @ManyToMany
    @JoinTable(
        name = "family_members",
        joinColumns = @JoinColumn(name = "family_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"family_id", "user_id"})
    )
    private Set<User> members;
}