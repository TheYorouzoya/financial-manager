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
    
    @OneToMany(mappedBy = "family")
    private Set<User> members;

    @PreRemove
    private void preRemove() {
        members.forEach(member -> member.setFamily(null));
    }

    public void addMember(User member) {
        this.members.add(member);
    }
}