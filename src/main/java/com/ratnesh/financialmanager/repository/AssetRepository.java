package com.ratnesh.financialmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratnesh.financialmanager.model.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByUser_Id(UUID userId);
}