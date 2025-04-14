package com.ratnesh.financialmanager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteByUser(User user);
}
