package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.dto.QSignupDTO;
import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.dditlms.domain.entity.QOpenLecture.*;
import static com.example.dditlms.domain.entity.QSubject.*;

public class SignupSearchRepositoryImpl implements SignupSearchRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SignupSearchRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<SignupDTO> totalLectureList() {
        return queryFactory
                .select(new QSignupDTO(openLecture.yearSeme.year,
                        QMajor.major.korean,
                        QOpenLecture.openLecture.lectureSection,
                        QOpenLecture.openLecture.id,
                        QSubject.subject.name,
                        QMember.member.name,
                        QOpenLecture.openLecture.lectureSchedule,
                        openLecture.lectureId.id,
                        subject.point,
                        QOpenLecture.openLecture.peopleNumber))
                .from(QOpenLecture.openLecture,
                        QMajor.major,
                        QSubject.subject,
                        QMember.member,
                        QProfessor.professor)
                .where(QOpenLecture.openLecture.majorCode.eq(QMajor.major)
                        .and(QOpenLecture.openLecture.subjectCode.eq(QSubject.subject))
                        .and(QOpenLecture.openLecture.professorNo.eq(QProfessor.professor))
                        .and(QProfessor.professor.member.eq(QMember.member)))
                .fetch();
    }





}
