package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Student;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.dditlms.domain.entity.QHistory.history;
import static com.example.dditlms.domain.entity.QTempAbsence.tempAbsence;


public class HistoryRepositoryImpl implements HistoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public HistoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<History> getAll() {
        return queryFactory
                .select(history)
                .from(history, tempAbsence)
                .innerJoin(tempAbsence)
                .on(history.tempAbsence.eq(tempAbsence))
                .fetch();
    }

    @Override
    public List<History> getfindAllByStudent(Student student) {
        return queryFactory
                .select(history)
                .from(history)
                .where(history.student.eq(student))
                .innerJoin(tempAbsence)
                .on(history.tempAbsence.eq(tempAbsence))
                .orderBy(history.aplicationDate.asc())
                .fetch();
    }



}
