package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.AccountDTO;
import com.ratnesh.financialmanager.mapper.AccountMapper;
import com.ratnesh.financialmanager.model.Account;
import com.ratnesh.financialmanager.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<AccountDTO> getAccountById(UUID id) {
        return accountRepository.findById(id).map(accountMapper::toDTO);
    }
    
    @Transactional
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        Account account = accountMapper.toEntity(accountDTO);
        return accountMapper.toDTO(accountRepository.save(account));
    }
    
    @Transactional
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }
    
    public List<AccountDTO> getAccountsByUserId(UUID userId) {
        return accountRepository.findByUser_Id(userId).stream()
                .map(accountMapper::toDTO)
                .collect(Collectors.toList());
    }
}