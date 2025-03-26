package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.TransactionDTO;
import com.ratnesh.financialmanager.mapper.TransactionMapper;
import com.ratnesh.financialmanager.model.Transaction;
import com.ratnesh.financialmanager.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<TransactionDTO> getTransactionById(UUID id) {
        return transactionRepository.findById(id).map(transactionMapper::toDTO);
    }
    
    @Transactional
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }
    
    @Transactional
    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }
    
    public List<TransactionDTO> getTransactionsByAccountId(UUID accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }
}