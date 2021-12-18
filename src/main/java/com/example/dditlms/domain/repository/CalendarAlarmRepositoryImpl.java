package com.example.dditlms.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class CalendarAlarmRepositoryImpl implements CalendarAlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CalendarAlarmRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

}
