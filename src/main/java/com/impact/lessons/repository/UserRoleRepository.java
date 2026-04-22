package com.impact.lessons.repository;

import com.impact.lessons.entity.UserRole;
import com.impact.lessons.entity.UserRoleId;
import com.impact.lessons.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    Optional<UserRole> findByUserId(Long userId);
}
