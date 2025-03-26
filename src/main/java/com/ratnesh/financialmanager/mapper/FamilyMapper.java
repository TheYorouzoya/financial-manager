package com.ratnesh.financialmanager.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ratnesh.financialmanager.dto.family.FamilyDTO;
import com.ratnesh.financialmanager.model.Family;
import com.ratnesh.financialmanager.model.User;

@Mapper( imports = User.class, componentModel = "spring" )
public interface FamilyMapper {
    @Mapping(target = "members", source = "members", qualifiedByName = "mapMembersToIds")
    FamilyDTO toDTO(Family family);

    @Mapping(target = "members", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Family toEntity(FamilyDTO familyDTO);

    @Named("mapMembersToIds")
    default Set<UUID> mapMembersToIds(Set<User> members) {
        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }
        return members.stream().map(User::getId).collect(Collectors.toSet());
    }
}