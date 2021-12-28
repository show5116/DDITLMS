package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.MajorSelection;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.QMajor;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class MajorRepositoryImpl implements MajorRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MajorRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Major> collegeMajorList(String majorSelection){
        return queryFactory
                .select(QMajor.major)
                .from(QMajor.major)
                .where(QMajor.major.selection.eq(MajorSelection.valueOf(majorSelection)))
                .fetch();
    }






}
