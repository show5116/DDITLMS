package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.example.dditlms.domain.entity.QOpenLecture.openLecture;
import static com.example.dditlms.domain.entity.QSubject.subject;

public class PreCourseRegistrationRepositoryImpl implements PreCourseRegistrationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PreCourseRegistrationRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }








}
