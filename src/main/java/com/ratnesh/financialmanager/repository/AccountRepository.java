package com.ratnesh.financialmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ratnesh.financialmanager.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUser_Id(UUID userId);
}