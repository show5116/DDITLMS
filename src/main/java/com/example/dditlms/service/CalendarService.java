package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import org.springframework.stereotype.Service;

public interface CalendarService {

    public boolean addSchedule(Calendar calendar);

    public boolean addAlarm(CalendarAlarm calendarAlarm);


}
