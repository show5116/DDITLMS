package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.ScholarshipKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScholarshipKindRepository extends JpaRepository<ScholarshipKind,String> {
}
