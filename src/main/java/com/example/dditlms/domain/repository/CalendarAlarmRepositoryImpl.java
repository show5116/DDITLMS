package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.QCalendarAlarm;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class CalendarAlarmRepositoryImpl implements CalendarAlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CalendarAlarmRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public JPADeleteClause deleteAlarm(Calendar calendar){
        return queryFactory
                .delete(QCalendarAlarm.calendarAlarm)
                .where(QCalendarAlarm.calendarAlarm.calendar.id.eq(calendar.getId()));

    };

}
