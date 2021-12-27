package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.OpenSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupSearchRepository extends JpaRepository<OpenSubject, String>, SignupSearchRepositoryCustom {
}
