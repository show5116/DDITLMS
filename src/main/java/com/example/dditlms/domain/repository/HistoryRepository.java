package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History,Long> {
    Optional<History> findByStudentAndStatusAndResultStatus(Student student, AcademicStatus status, ResultStatus resultStatus);

}
