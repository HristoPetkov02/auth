package com.tinqinacademy.auth.persistence.repository;

import com.tinqinacademy.auth.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
