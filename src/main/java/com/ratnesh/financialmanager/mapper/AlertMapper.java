package com.ratnesh.financialmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ratnesh.financialmanager.dto.AlertDTO;
import com.ratnesh.financialmanager.model.Alert;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(source = "user.id", target = "userId")
    AlertDTO toDTO(Alert alert);
    
    @Mapping(target = "user", ignore = true) // Ignore full object when mapping back
    Alert toEntity(AlertDTO alertDTO);
}