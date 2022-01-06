package com.example.dditlms.config.scheduler;

import com.example.dditlms.domain.dto.SMSDTO;
import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.repository.CalendarAlarmRepository;
import com.example.dditlms.service.CalendarAlarmService;
import com.example.dditlms.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class scheduler {

    private final CalendarAlarmService service;
    private final CalendarAlarmRepository calendarAlarmRepository;

    private final SMSService smsService;
    //cron 규칙 (초 분 시 일 월 요일 연도(생략가능))
    //(0 0/1 * * * *) 매 1분마다 실행
    @Scheduled(fixedDelay=60000)
    public void testCron(){
        List<Map<String,Object>> calendarAlarmList = service.getAlarmList();
        for (Map<String,Object> map : calendarAlarmList){
            String alarmTime = (String) map.get("alarmTime");
            String type = (String)map.get("type");
            String phone = (String)map.get("phone");
            LocalDateTime parse = LocalDateTime.parse(alarmTime);
            LocalDateTime nowDateTime = LocalDateTime.now();
            LocalDateTime plusParse = parse.plusMinutes(1L);
            if (nowDateTime.isAfter(parse) && nowDateTime.isBefore(plusParse) && type.equals("SMS")){
                SMSDTO smsdto = SMSDTO.builder()
                        .to(phone)
                        .from("01051161830")
                        .type("SMS")
                        .text(map.get("text")+"")
                        .app_version("test app 1.2").build();
                smsService.SendMessage(smsdto);
            }
        }
    }
}
