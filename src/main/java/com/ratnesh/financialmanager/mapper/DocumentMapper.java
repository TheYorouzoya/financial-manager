// MapStruct Mappers
package com.ratnesh.financialmanager.mapper;

import com.ratnesh.financialmanager.dto.*;
import com.ratnesh.financialmanager.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "user.id", target = "userId")
    DocumentDTO toDTO(Document document);

    @Mapping(target = "family", ignore = true) 
    @Mapping(target = "user", ignore = true)
    Document toEntity(DocumentDTO documentDTO);
}