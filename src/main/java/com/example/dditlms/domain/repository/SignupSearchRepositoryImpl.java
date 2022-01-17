package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.common.MajorSelection;
import com.example.dditlms.domain.dto.QSignupDTO;
import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.dditlms.domain.entity.QOpenLecture.openLecture;
import static com.example.dditlms.domain.entity.QSubject.subject;


@Slf4j
public class SignupSearchRepositoryImpl implements SignupSearchRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SignupSearchRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<OpenLecture> searchSubject(String searchSubject){
        return queryFactory
                .select(openLecture)
                .from(openLecture, subject)
                .where(openLecture.subjectCode.eq(subject)
                        ,QSubject.subject.name.contains(searchSubject))
                .fetch();
    }

    @Override
    public List<SignupDTO> totalLectureList(Map<String,Object> searchSubject) {
        JPAQuery<SignupDTO> query = queryFactory
                                        .select(new QSignupDTO(openLecture.yearSeme.year,
                                                QMajor.major.korean,
                                                openLecture.lectureSection,
                                                openLecture.id,
                                                subject.name,
                                                QMember.member.name,
                                                openLecture.lectureSchedule,
                                                openLecture.lectureId.id,
                                                subject.point,
                                                openLecture.peopleNumber,
                                                openLecture.syllabusFileId))
                                        .from(openLecture,
                                                QMajor.major,
                                                subject,
                                                QMember.member,
                                                QProfessor.professor)
                                        .where(openLecture.majorCode.eq(QMajor.major)
                                                , openLecture.subjectCode.eq(subject)
                                                , openLecture.professorNo.eq(QProfessor.professor)
                                                ,QProfessor.professor.member.eq(QMember.member));
        List<SignupDTO> result = new ArrayList<>();

        String methodName = (String)searchSubject.get("name");
        switch (methodName){
            case "totalList" :
               query.orderBy(openLecture.yearSeme.year.asc());
               result = query.fetch();
               break;
            case "searchSubject" :
                String subject = (String)searchSubject.get("subject");
                query.where(QSubject.subject.name.contains(subject));
                query.orderBy(openLecture.yearSeme.year.asc());
                result = query.fetch();
                break;
            case "allAutoSearch" :
                String allsearchYear = (String) searchSubject.get("searchYear");
                String allsearchSeme = (String) searchSubject.get("searchSeme");
                String allyearSeme = "-"+allsearchSeme;
                String allcollege = (String) searchSubject.get("college");
                String allmajor = (String)searchSubject.get("major");
                String alldivision = (String)searchSubject.get("division");
                log.info("-------REPOSITORY[totalLectureList] :: college = {}", allcollege);
                log.info("-------REPOSITORY[totalLectureList] :: major = {}", allmajor);

                if (allcollege.equals("total")){
                    log.info("-------REPOSITORY[totalLectureList] :: college==total = {}", allcollege.equals("total"));
                    allcollege = "";
                }else {
                    query.where(QMajor.major.selection.eq(MajorSelection.valueOf(allcollege)));
                }

                if (allmajor.equals("total")){
                    allmajor = "";
                }else {
                    query.where(QMajor.major.korean.eq(allmajor));
                }

                if (alldivision.equals("total")){
                    alldivision = "";
                }else {
                    LectureSection lectureSection = LectureSection.valueOf(alldivision);
                    query.where(openLecture.lectureSection.eq(lectureSection));
                }
                query.where(openLecture.yearSeme.year.contains(allsearchYear));
                query.where(openLecture.yearSeme.year.contains(allyearSeme));
                query.orderBy(openLecture.yearSeme.year.asc());
                result = query.fetch();
                break;
        }
        return result;
    }
}