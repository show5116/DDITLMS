package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Enrolment;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Student> {
    List<Enrolment> findAllByStudent(Student student);

}
