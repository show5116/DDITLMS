package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.SemesterByYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupSearchRepository extends JpaRepository<OpenLecture, String>, SignupSearchRepositoryCustom {
    List<OpenLecture> findAllByYearSeme(SemesterByYear semester);

}
