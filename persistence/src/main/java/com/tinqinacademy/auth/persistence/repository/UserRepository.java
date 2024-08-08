package com.tinqinacademy.auth.persistence.repository;

import com.tinqinacademy.auth.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
