package com.example.dditlms.domain.repository.sanctn;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.example.dditlms.domain.entity.sanction.QDocform.docform;

public class DocformRepositoryImpl implements DocformRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DocformRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

}
