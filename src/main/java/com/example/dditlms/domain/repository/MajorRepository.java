package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, String> {
    Optional<Major> findById(String id);
}
