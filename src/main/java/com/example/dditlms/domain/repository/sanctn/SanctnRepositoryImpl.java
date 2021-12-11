package com.example.dditlms.domain.repository.sanctn;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class SanctnRepositoryImpl implements SanctnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

}
