package com.example.dditlms.service;

import com.example.dditlms.domain.entity.CalendarAlarm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CalendarAlarmService {
    @Transactional(readOnly = true)
    List<Map<String,Object>> getAlarmList();
}
