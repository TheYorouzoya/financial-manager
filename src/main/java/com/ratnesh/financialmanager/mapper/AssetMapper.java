package com.ratnesh.financialmanager.mapper;


import com.ratnesh.financialmanager.dto.AssetDTO;
import com.ratnesh.financialmanager.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "user.id", target = "userId")
    AssetDTO toDTO(Asset asset);

    @Mapping(target = "family", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    Asset toEntity(AssetDTO assetDTO);
}