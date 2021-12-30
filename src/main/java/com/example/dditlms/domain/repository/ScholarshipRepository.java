package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship,Long> {
    List<Scholarship> findAllByStudentAndSemesterAndStatusNot(Student student, SemesterByYear semester, ResultStatus status);

    List<Scholarship> findAllByStudentAndSemesterAndStatusAndMethod(Student student, SemesterByYear semester, ResultStatus status, ScholarshipMethod method);

    List<Scholarship> findAllByStudentAndStatus(Student student, ResultStatus status);

    List<Scholarship> findAllByStudentAndStatusNot(Student student, ResultStatus status);
}
