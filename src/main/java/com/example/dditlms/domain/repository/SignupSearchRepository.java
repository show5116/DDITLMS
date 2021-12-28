package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.OpenLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupSearchRepository extends JpaRepository<OpenLecture, String>, SignupSearchRepositoryCustom {
}
