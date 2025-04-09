package com.ratnesh.financialmanager.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ratnesh.financialmanager.dto.role.RoleDTO;
import com.ratnesh.financialmanager.dto.role.RoleResponseDTO;
import com.ratnesh.financialmanager.model.Privilege;
import com.ratnesh.financialmanager.model.Role;
import com.ratnesh.financialmanager.model.User;

@Mapper( imports = { User.class, Privilege.class }, componentModel = "spring")
public interface RoleMapper {
    
    @Mapping(target = "users", source = "users", qualifiedByName = "mapUsersToIds")
    @Mapping(target = "privileges", source = "privileges", qualifiedByName = "mapPrivilegesToIds")
    RoleDTO toDTO(Role role);

    @Mapping(target = "privileges", source = "privileges", qualifiedByName = "mapPrivilegesToNames")
    RoleResponseDTO toRoleResponseDTO(Role role);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "privileges", ignore = true)
    Role toEntity(RoleDTO roleDTO);

    @Named("mapUsersToIds")
    default List<UUID> mapUsersToIds(Collection<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream().map(User::getId).toList();
    }

    @Named("mapPrivilegesToIds")
    default List<UUID> mapPrivilegesToIds(Collection<Privilege> privileges) {
        if (privileges == null || privileges.isEmpty()) {
            return Collections.emptyList();
        }
        return privileges.stream().map(Privilege::getId).toList();
    }

    @Named("mapPrivilegesToNames")
    default List<String> mapPrivilegesToNames(Collection<Privilege> privileges) {
        if (privileges == null || privileges.isEmpty()) {
            return Collections.emptyList();
        }
        return privileges.stream().map(Privilege::getName).toList();
    }
}