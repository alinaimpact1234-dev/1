package com.impact.lessons.repository;

import com.impact.lessons.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonalDataRepository extends JpaRepository<UserPersonalData, Long> {}

