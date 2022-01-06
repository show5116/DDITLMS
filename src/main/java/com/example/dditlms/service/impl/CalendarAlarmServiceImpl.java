package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.repository.CalendarAlarmRepository;
import com.example.dditlms.service.CalendarAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalendarAlarmServiceImpl implements CalendarAlarmService {
    private final CalendarAlarmRepository calendarAlarmRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String,Object>> getAlarmList(){
        List<Map<String,Object>> alarmList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        String now = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        List<CalendarAlarm> calendarAlarmList = calendarAlarmRepository.findAllByScheduleAlarmTimeLike(date+"%");
        for(CalendarAlarm alarm : calendarAlarmList){
            Map<String,Object> map = new HashMap<>();
            map.put("alarmTime", alarm.getScheduleAlarmTime());
            map.put("phone", alarm.getCalendar().getMember().getPhone());
            map.put("text",madeSendMassage(alarm,alarm.getCalendar()));
            map.put("type", alarm.getScheduleAlarmType());
            alarmList.add(map);
        }
        return alarmList;
    }

    public String madeSendMassage(CalendarAlarm alarm, Calendar calendar){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M월d일 a h시m분");
        LocalDateTime date = LocalDateTime.parse(alarm.getCalendar().getScheduleStr());
        StringBuilder sb = new StringBuilder();
        sb.append("DDIT 일정알림 서비스입니다. \n\n");
        sb.append("> ");
        sb.append(date.format(dateTimeFormatter));
        sb.append("\n");
        sb.append("> ");
        sb.append(alarm.getScheduleContent());
        sb.append("\n");
        if (calendar.getScheduleLocation() != null){
            sb.append("> ");
            sb.append(calendar.getScheduleLocation());
        }
        return sb.toString();
    }
}
