package com.ratnesh.financialmanager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratnesh.financialmanager.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
    Optional<Privilege> findByName(String name);
}