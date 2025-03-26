package com.ratnesh.financialmanager.mapper;

import com.ratnesh.financialmanager.dto.*;
import com.ratnesh.financialmanager.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "account.id", target = "accountId")
    TransactionDTO toDTO(Transaction transaction);

    @Mapping(target = "account", ignore = true) // Ignore full object 
    Transaction toEntity(TransactionDTO transactionDTO);
}