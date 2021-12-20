package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface CalendarService {
    // 일정등록
    public void addSchedule(Map<String, Object> paramMap);

    //일정 삭제 (return true:본인일정이라 삭제성공
    public boolean deleteSchedule(Calendar calendar);

}
