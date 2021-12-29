package com.example.dditlms.domain.repository.student;

import com.example.dditlms.domain.entity.QRegistration;
import com.example.dditlms.domain.entity.QStudent;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QRegistration.*;
import static com.example.dditlms.domain.entity.QStudent.*;

public class StudentRepositoryImpl implements StudentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public StudentRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

}
