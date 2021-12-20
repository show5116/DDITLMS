package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.dditlms.domain.entity.QMajor.major;
import static com.example.dditlms.domain.entity.QStudent.student;

@Slf4j
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

    @Override
    public List<Calendar> getAllScheduleList(Member userNumber) {
        return queryFactory
                .select(QCalendar.calendar)
                .from(QCalendar.calendar)
                .where(QCalendar.calendar.member.eq(userNumber)
                        .or(QCalendar.calendar.scheduleType.eq("TOTAL"))
                        .or((QCalendar.calendar.scheduleType.eq("MAJOR"))
                                .and(QCalendar.calendar.scheduleTypeDetail.eq(queryFactory
                                                                            .select(major.korean)
                                                                            .from(student, major)
                                                                            .where(student.member.eq(userNumber), student.major.eq(major))))))
                .fetch();
    }

    @Override
    public int countConfirmScheduleWriter(Calendar calendar) {
        int count =(int) queryFactory
                            .select(QCalendar.calendar)
                            .from(QCalendar.calendar)
                            .where(QCalendar.calendar.id.eq(calendar.getId()),
                                    QCalendar.calendar.member.eq(calendar.getMember()))
                            .fetchCount();
        log.info("---------------" + count+"");
        return count;
    }

    @Override
    public List<String> getAllMajorList() {
        return queryFactory
                .select(major.korean)
                .from(major)
                .fetch();
    }

    @Override
    public Long getLastScheduleNumber(){
        return queryFactory
                .select(QCalendar.calendar.id.max())
                .from(QCalendar.calendar)
                .fetchOne();
    }

    @Override
    public Calendar getSchedule(Long id){
        return queryFactory
                .select(QCalendar.calendar)
                .from(QCalendar.calendar)
                .where(QCalendar.calendar.id.eq(id))
                .fetchOne();
    }

}















