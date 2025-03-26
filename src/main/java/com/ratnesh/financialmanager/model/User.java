package com.ratnesh.financialmanager.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.*;

// User Entity
@Entity
@Table(name = "users")
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder
public class User {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePictureUrl;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private boolean active;
    
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;;
    
    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;
}