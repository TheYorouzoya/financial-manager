package com.ratnesh.financialmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import com.ratnesh.financialmanager.model.Family;

public interface FamilyRepository extends JpaRepository<Family, UUID> {
    Optional<Family> findByMembers_Id(UUID userId);
}