package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;

import java.util.List;

public interface CalendarAlarmRepositoryCustom {

    public List<Long> getAlarmId(Calendar calendar);

    public List<String> findAlarmType(Long scheduleId);

    public List<CalendarAlarm> getAlarmSchedule(Long scheduleId);
}
