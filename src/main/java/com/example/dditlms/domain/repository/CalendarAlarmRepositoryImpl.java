package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.QCalendarAlarm;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class CalendarAlarmRepositoryImpl implements CalendarAlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CalendarAlarmRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Long getAlarmId(Calendar calendar){
        return queryFactory
                .select(QCalendarAlarm.calendarAlarm.id)
                .from(QCalendarAlarm.calendarAlarm)
                .where(QCalendarAlarm.calendarAlarm.calendar.id.eq(calendar.getId()))
                .fetchOne();
    }

    @Override
    public List<String> findAlarmType(Long scheduleId){
         return queryFactory
                 .select(QCalendarAlarm.calendarAlarm.scheduleAlarmType)
                 .from(QCalendarAlarm.calendarAlarm)
                 .where(QCalendarAlarm.calendarAlarm.calendar.id.eq(scheduleId))
                 .fetch();
    }


}
