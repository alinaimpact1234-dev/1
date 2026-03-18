package com.impact.lessons.repository;

import com.impact.lessons.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByUsername(String username);
}

