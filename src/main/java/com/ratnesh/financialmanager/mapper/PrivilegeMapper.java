package com.ratnesh.financialmanager.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ratnesh.financialmanager.dto.PrivilegeDTO;
import com.ratnesh.financialmanager.model.Privilege;
import com.ratnesh.financialmanager.model.Role;

@Mapper( imports = Role.class, componentModel = "spring" )
public interface PrivilegeMapper {
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToIds")
    PrivilegeDTO toDTO(Privilege privilege);

    @Mapping(target = "roles", ignore = true)
    Privilege toEntity(PrivilegeDTO privilegeDTO);

    @Named("mapRolesToIds")
    default Set<UUID> mapRolesToIds(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        return roles.stream().map(Role::getId).collect(Collectors.toSet());
    }
}