package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.dditlms.domain.entity.QMajor.major;
import static com.example.dditlms.domain.entity.QStudent.student;

public class CalendarRepositoryImpl implements CalendarRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CalendarRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Student getMajor(Long userNumber) {

        return queryFactory
                .select(student)
                .from(student)
                .from(major)
                .join(student.major, major)
                .where(student.major.eq(major), student.userNumber.eq(userNumber))
                .fetchOne();
    }

//    public List<Calendar> getScheduleList(Member userNumber, String scheduleType) {
//        return queryFactory
//                .select(QCalendar.calendar)
//                .from(QCalendar.calendar)
//                .where(QCalendar.calendar.member.eq(userNumber)
//                        .or((QCalendar.calendar.scheduleType.eq(scheduleType))
//                        .and(QCalendar.calendar.scheduleTypeDetail.eq(queryFactory
//                                                                            .select(major.korean)
//                                                                            .from(major, student)
//                                                                            .where(student.member.eq(userNumber), student.major.eq(major))))))
//                .fetch();
//    }
        public List<Calendar> getAllScheduleList(Member userNumber, String scheduleType) {
            return queryFactory
                    .select(QCalendar.calendar)
                    .from(QCalendar.calendar)
                    .where(QCalendar.calendar.member.eq(userNumber)
                            .or(QCalendar.calendar.scheduleType.eq("TOTAL"))
                            .or((QCalendar.calendar.scheduleType.eq(scheduleType))
                                    .and(QCalendar.calendar.scheduleTypeDetail.eq(queryFactory
                                                                                .select(major.korean)
                                                                                .from(student, major)
                                                                                .where(student.member.eq(userNumber), student.major.eq(major))))))
                    .fetch();
        }

}
