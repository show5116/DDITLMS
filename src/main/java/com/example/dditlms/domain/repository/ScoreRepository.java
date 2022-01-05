package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.Score;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByEnrolment_StudentAndEnrolment_OpenLecture(Student student, OpenLecture openLecture);
    Optional<List<Score>> findAllByEnrolment_OpenLecture(OpenLecture openLecture);
}
