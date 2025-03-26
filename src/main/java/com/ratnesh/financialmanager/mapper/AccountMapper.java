package com.ratnesh.financialmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.ratnesh.financialmanager.dto.AccountDTO;
import com.ratnesh.financialmanager.model.Account;
import com.ratnesh.financialmanager.model.AccountType;
import com.ratnesh.financialmanager.service.UserService;

@Mapper(componentModel = "spring", uses = { UserService.class })
public interface AccountMapper {
    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "user.id", target = "userId")
    AccountDTO toDTO(Account account);

    @Mapping(target = "family", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "type", target = "type", qualifiedByName = "stringToEnum")
    Account toEntity(AccountDTO accountDTO);

    @Named("stringToEnum")
    default AccountType stringToEnum(String type) {
        return AccountType.valueOf(type.toUpperCase());
    }
}

