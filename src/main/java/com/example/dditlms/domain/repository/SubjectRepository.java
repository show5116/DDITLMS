package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.WheatherToDelete;
import com.example.dditlms.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject,String> {
    List<Subject> findAllByStatus(WheatherToDelete status);
}
