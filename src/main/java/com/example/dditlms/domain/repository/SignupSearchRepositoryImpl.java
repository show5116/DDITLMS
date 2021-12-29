package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.MajorSelection;
import com.example.dditlms.domain.dto.QSignupDTO;
import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.dditlms.domain.entity.QOpenLecture.*;
import static com.example.dditlms.domain.entity.QSubject.*;

@Slf4j
public class SignupSearchRepositoryImpl implements SignupSearchRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SignupSearchRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
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
                                                openLecture.peopleNumber))
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
               result = query.fetch();
               break;
            case "searchSubject" :
                String subject = (String)searchSubject.get("subject");
                query.where(QSubject.subject.name.contains(subject));
                result = query.fetch();
                break;
            case "searchYear" :
                String searchYear = (String) searchSubject.get("searchYear");
                String searchSeme = (String) searchSubject.get("searchSeme");
                String yearSeme = "-"+searchSeme;
                log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case3 :: yearSeme ={}",yearSeme);
                query.where(openLecture.yearSeme.year.contains(searchYear));
                query.where(openLecture.yearSeme.year.contains(yearSeme));
                result = query.fetch();
                break;
            case "searchMajor" :
                String college = (String) searchSubject.get("college");
                String major = (String)searchSubject.get("major");
                log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4 :: major ={}",major);
                if (major ==null || major.equals("")){
                    major = "";
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-if문 :: major ={}",major);
                }else {
                    query.where(QMajor.major.korean.eq(major));
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-else문 :: major ={}",major);
                }
                query.where(QMajor.major.selection.eq(MajorSelection.valueOf(college)));
                result = query.fetch();
                break;
            case "searchCollege" :
                String searchCollege = (String) searchSubject.get("college");
                query.where(QMajor.major.selection.eq(MajorSelection.valueOf(searchCollege)));
                result = query.fetch();
                break;
            case "allAutoSearch" :
                String allsearchYear = (String) searchSubject.get("searchYear");
                String allsearchSeme = (String) searchSubject.get("searchSeme");
                String allyearSeme = "-"+allsearchSeme;
                String allcollege = (String) searchSubject.get("college");
                String allmajor = (String)searchSubject.get("major");
                String alldivision = (String)searchSubject.get("division");

                if (allcollege.equals("total")){
                    allcollege = "";
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-if문 :: allcollege ={}",allcollege);
                }else {
                    query.where(QMajor.major.selection.eq(MajorSelection.valueOf(allcollege)));
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-else문 :: allcollege ={}",allcollege);
                }

                if (allmajor.equals("total")){
                    allmajor = "";
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-if문 :: major ={}",allmajor);
                }else {
                    query.where(QMajor.major.korean.eq(allmajor));
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-else문 :: major ={}",allmajor);
                }

                if (alldivision.equals("total")){
                    alldivision = "";
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-if문 :: alldivision ={}",allmajor);
                }else {
                    query.where(openLecture.lectureSection.eq(alldivision));
                    log.info("-----signupSearchRepositoryImpl-totalLectureList :: In switch-case4-else문 :: alldivisionS ={}",allmajor);
                }


                query.where(openLecture.yearSeme.year.contains(allsearchYear));
                query.where(openLecture.yearSeme.year.contains(allyearSeme));
                result = query.fetch();
                break;
        }

        return result;
    }







}
