package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.Student;
import com.example.dditlms.domain.entity.TempAbsence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TempAbsenceRepository extends JpaRepository<TempAbsence, Long> {
//    Optional<TempAbsence> findByMberNoAndStatus(Student student, ResultStatus resultStatus);
//    List<TempAbsence> findAllByMberNo(Student student);
}
