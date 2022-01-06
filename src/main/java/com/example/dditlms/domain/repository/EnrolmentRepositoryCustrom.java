package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Enrolment;
import com.example.dditlms.domain.entity.Student;

import java.util.List;

public interface EnrolmentRepositoryCustrom {
    List<Enrolment> myPregidentList(Student student, String yearSeme);

    int findSameSubjectCode(Student student, String subject);
}
