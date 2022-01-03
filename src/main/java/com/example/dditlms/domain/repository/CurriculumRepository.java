package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.entity.Curriculum;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurriculumRepository extends JpaRepository<Curriculum,Long> {
    List<Curriculum> findAllBySemesterAndGradeAndMajorAndLectureSection(SemesterByYear semester, Grade grade, Major major, LectureSection section);

    Optional<Curriculum> findBySemesterAndGradeAndMajorAndSubject(SemesterByYear semester, Grade grade, Major major, Subject subject);
}
