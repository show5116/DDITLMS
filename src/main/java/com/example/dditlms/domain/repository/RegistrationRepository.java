package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Registration;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    List<Registration> findAllByStudentOrderByRegistDateDesc(Student student);
}
