package com.example.dditlms.service;

import com.example.dditlms.domain.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    public List<StudentDTO> getNotRegistedStudents();

    public List<StudentDTO> searchNotRegistedStudents(String category,String search);

    public void notPayTuition(Long id);

    public void payTuition(Long id);

}
