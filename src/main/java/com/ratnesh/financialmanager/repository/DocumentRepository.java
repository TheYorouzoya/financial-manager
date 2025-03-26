package com.ratnesh.financialmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratnesh.financialmanager.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByUser_Id(UUID userId);
    List<Document> findByFamily_Id(UUID familyId);
}