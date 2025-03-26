package com.ratnesh.financialmanager.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    
    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
    
    public enum CategoryType {
        INCOME, HOUSING, TRANSPORTATION, FOOD, HEALTHCARE, PERSONAL, ENTERTAINMENT, DEBT, SAVINGS, MISCELLANEOUS
    }
}