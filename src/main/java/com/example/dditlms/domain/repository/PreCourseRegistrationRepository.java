package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.PreCourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreCourseRegistrationRepository  extends JpaRepository<PreCourseRegistration, Long> {
}
