package com.ratnesh.financialmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.ratnesh.financialmanager.dto.user.UserDTO;
import com.ratnesh.financialmanager.dto.user.UserRegistrationDTO;
import com.ratnesh.financialmanager.dto.user.UserResponseDTO;
import com.ratnesh.financialmanager.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "family.id", target = "familyId")
    UserDTO toDTO(User user);

    UserResponseDTO toUserResponseDTO(User user);

    User toEntity(UserDTO userDTO);

    User toEntity(UserRegistrationDTO userRegistrationDTO);

    User updateUserFromDTO(UserRegistrationDTO userDTO, @MappingTarget User user);
}