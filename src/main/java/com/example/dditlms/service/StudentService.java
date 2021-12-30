package com.example.dditlms.service;

import com.example.dditlms.domain.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    public List<StudentDTO> getNotRegistedStudents();

    public void notPayTuition(Long id);

    public void payTuition(Long id);

}
