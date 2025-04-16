package com.ratnesh.financialmanager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ratnesh.financialmanager.model.RefreshToken;
import com.ratnesh.financialmanager.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteByUser(User user);

    @Modifying
    @Query("delete from RefreshToken rt where rt.user.id = :userId")
    int deleteAllByUserId(@Param("userId") UUID userId);
}
