package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;

import java.util.List;

public interface CalendarAlarmRepositoryCustom {

    public Long getAlarmId(Calendar calendar);

    public List<String> findAlarmType(Long scheduleId);
}
