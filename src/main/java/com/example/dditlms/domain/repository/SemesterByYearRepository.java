package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.SemesterByYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterByYearRepository extends JpaRepository<SemesterByYear, String> {
}
