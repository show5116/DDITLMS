package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.SemesterByYear;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignupSearchRepository extends JpaRepository<OpenLecture, String>, SignupSearchRepositoryCustom {
    List<OpenLecture> findAllByYearSeme(SemesterByYear semester);
    List<OpenLecture> findAllByYearSemeAndMajorCode(SemesterByYear semester, Major major);
    List<OpenLecture> findAllByYearSemeAndLectureSection(SemesterByYear semester, LectureSection division);
    List<OpenLecture> findAllByYearSemeAndLectureSectionAndMajorCode(SemesterByYear semester,LectureSection division, Major major);



}
