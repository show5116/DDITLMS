package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.repository.CalendarAlarmRepository;
import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarAlarmRepository calendarAlarmRepository;


    @Transactional
    @Override
    public Calendar addAlarm(Calendar calendar,CalendarAlarm calendarAlarm) {
        calendarRepository.save(calendar);
        calendarAlarmRepository.save(calendarAlarm);
        return calendar;
    }

    @Override
    @Transactional
    public Calendar addSchedule(Calendar calendar) {
        calendarRepository.save(calendar);
        return calendar;
    }

    @Override
    public boolean deleteSchedule(Calendar calendar) {

        int count = calendarRepository.countConfirmScheduleWriter(calendar);

        System.out.println("count : "+ count);

        if (count == 1 ){
            calendarRepository.delete(calendar);
            return true;
        } else {
            return false;
        }

    }
}
