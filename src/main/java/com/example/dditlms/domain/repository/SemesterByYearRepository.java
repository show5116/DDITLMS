package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.SemesterByYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterByYearRepository extends JpaRepository<SemesterByYear, String> {

    @Query(nativeQuery = true,value = "SELECT * FROM (SELECT * FROM YEAR_SEME ys2 WHERE YEAR_SEME_START > SYSDATE ORDER BY YEAR_SEME_START) ys WHERE ROWNUM = 1")
    Optional<SemesterByYear> selectNextSeme();

    Optional<SemesterByYear> findBySemeStartLessThanEqualAndSemeEndGreaterThanEqual(Date now,Date now2);

    List<SemesterByYear> findAllByOrderByYearDesc();
}