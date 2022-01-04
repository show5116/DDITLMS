package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Enrolment;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.QEnrolment;
import com.example.dditlms.domain.entity.Student;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
public class EnrolmentRepositoryImpl implements EnrolmentRepositoryCustrom{

    private final JPAQueryFactory queryFactory;

    public EnrolmentRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Enrolment> myPregidentList(Student student, String yearSeme){
        return queryFactory
                .selectFrom(QEnrolment.enrolment)
                .where(QEnrolment.enrolment.student.eq(student),
                        QEnrolment.enrolment.openLecture.yearSeme.year.eq(yearSeme))
                .fetch();
    }


}
