package com.tinqinacademy.auth.persistence.repository;

import com.tinqinacademy.auth.persistence.models.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlackListedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    boolean existsByToken(String token);
}
