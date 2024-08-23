package com.tinqinacademy.auth.persistence.repository;

import com.tinqinacademy.auth.persistence.models.User;
import com.tinqinacademy.auth.persistence.models.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserVerificationRepository extends JpaRepository<UserVerification, UUID> {
    Optional<UserVerification> findUserVerificationByUser(User user);
    Optional<UserVerification> findUserVerificationByVerificationCode(String verificationCode);
}
