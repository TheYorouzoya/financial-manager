package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.AssetDTO;
import com.ratnesh.financialmanager.mapper.AssetMapper;
import com.ratnesh.financialmanager.model.Asset;
import com.ratnesh.financialmanager.repository.AssetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    
    public List<AssetDTO> getAllAssets() {
        // TODO: Filter by authenticated user's family
        return assetRepository.findAll()
                .stream()
                .map(assetMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<AssetDTO> getAssetById(UUID id) {
        // TODO: Ensure the asset belongs to the authenticated user's family
        return assetRepository.findById(id).map(assetMapper::toDTO);
    }
    
    @Transactional
    public AssetDTO saveAsset(AssetDTO assetDTO) {
        // TODO: Associate asset with authenticated user's family
        Asset asset = assetMapper.toEntity(assetDTO);
        asset = assetRepository.save(asset);
        return assetMapper.toDTO(asset);
    }
    
    @Transactional
    public void deleteAsset(UUID id) {
        // TODO: Ensure the asset belongs to the authenticated user's family before deletion
        assetRepository.deleteById(id);
    }
    
    // Stub for authentication
    // private Long getAuthenticatedUserId() {
    //     return null; // TODO: Retrieve actual user ID from SecurityContextHolder
    // }
}