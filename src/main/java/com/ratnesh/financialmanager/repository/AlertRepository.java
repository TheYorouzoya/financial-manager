package com.ratnesh.financialmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratnesh.financialmanager.model.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByUserId(UUID userId);
}
