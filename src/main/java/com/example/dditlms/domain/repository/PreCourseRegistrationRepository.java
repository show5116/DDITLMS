package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.PreCourseRegistration;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreCourseRegistrationRepository  extends JpaRepository<PreCourseRegistration, Long> {
    List<PreCourseRegistration> findByStudentNo(Student studentNo);
    PreCourseRegistration findByStudentNoAndLectureCode(Student studentNo, OpenLecture lectureCode);
}
