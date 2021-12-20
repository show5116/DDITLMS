package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.querydsl.jpa.impl.JPADeleteClause;

public interface CalendarAlarmRepositoryCustom {

    public JPADeleteClause deleteAlarm(Calendar calendar);
}
