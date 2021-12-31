package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Student;

import java.util.List;

public interface HistoryRepositoryCustom {

    public List<History> getAll();

    public List<History> getfindAllByStudent(Student student);


}