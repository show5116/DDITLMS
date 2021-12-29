package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.common.ScholarshipStatus;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship,Long> {
    List<Scholarship> findAllByStudentAndSemesterAndStatusNot(Student student, SemesterByYear semester, ScholarshipStatus status);

    List<Scholarship> findAllByStudentAndSemesterAndStatusAndMethod(Student student, SemesterByYear semester, ScholarshipStatus status, ScholarshipMethod method);

    List<Scholarship> findAllByStudentAndStatus(Student student,ScholarshipStatus status);

    List<Scholarship> findAllByStudentAndStatusNot(Student student,ScholarshipStatus status);
}
