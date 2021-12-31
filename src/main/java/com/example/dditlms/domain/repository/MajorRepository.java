package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.MajorSelection;
import com.example.dditlms.domain.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, String>, MajorRepositoryCustom {
    Optional<Major> findById(String id);
    Optional<Major> findByKorean(String name);
}
