package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.CalendarAlarmRepository;
import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.service.CalendarService;
import com.querydsl.jpa.impl.JPADeleteClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository repository;
    private final CalendarAlarmRepository alarmRepository;

    @Override
    @Transactional
    public void addSchedule(Map<String, Object> paramsMap) {

        /** 파라미터 조회 ***************************************************************************/
        Member member =(Member)paramsMap.get("member");
        String alarmTime =(String)paramsMap.get("alarmTime");
        String sms = (String) paramsMap.get("sms");
        String kakao =(String) paramsMap.get("kakao");
        String type = (String)paramsMap.get("scheduleType");
        String typeDetail =(String)paramsMap.get("scheduleTypeDetail");
        String title = (String)paramsMap.get("title");
        String content =(String)paramsMap.get("content");
        String place = (String)paramsMap.get("scheduleLocation");
        String scheduleStr = (String)paramsMap.get("startDate");
        String scheduleEnd = (String)paramsMap.get("endDate");

        /** 파라미터 생성 */
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Calendar calendar = null;
        CalendarAlarm calendarAlarmSMS = null;
        CalendarAlarm calendarAlarmKakako = null;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        String scheduleStart = null; //알람시간

        /** 로직 처리 구간 *******************************************************************/

        try {date = df.parse(scheduleStr);}catch (ParseException e){}
        cal.setTime(date);

        if (!alarmTime.equals("none")){
            switch (alarmTime){
                case "30": cal.add(java.util.Calendar.MINUTE, -30);
                    log.info("switch에서 30");
                    break;
                case "60": cal.add(java.util.Calendar.HOUR, -1);
                    break;
                case "1440": cal.add(java.util.Calendar.DATE, -1);
                    break;
                case "10080": cal.add(java.util.Calendar.DATE, -7);
                    break;
            }
            scheduleStart = df.format(cal.getTime());
            try {
                calendar = Calendar.builder()
                        .scheduleType(type)
                        .scheduleTypeDetail(typeDetail)
                        .title(title)
                        .content(content)
                        .scheduleLocation(place)
                        .scheduleStr(scheduleStr)
                        .scheduleEnd(scheduleEnd)
                        .setAlarmTime(alarmTime)
                        .member(member).build();
                calendarAlarmSMS = CalendarAlarm.builder()
                        .scheduleContent(title)
                        .scheduleAlarmTime(scheduleStart)
                        .scheduleAlarmType("SMS")
                        .calendar(calendar).build();
                calendarAlarmKakako = CalendarAlarm.builder()
                        .scheduleContent(title)
                        .scheduleAlarmTime(scheduleStart)
                        .scheduleAlarmType("KAKAO")
                        .calendar(calendar).build();
            }catch (Exception e){
            }

            if(sms.equals("true")){
                calendar = repository.save(calendar);
                paramsMap.put("calendar", calendar);
                alarmRepository.save(calendarAlarmSMS);
            }
            if(kakao.equals("true")){
                calendar = repository.save(calendar);
                paramsMap.put("calendar", calendar);
                alarmRepository.save(calendarAlarmKakako);
            }
        } else{
            calendar = repository.save(calendar);
            paramsMap.put("calendar", calendar);
        }

        List<Calendar> scheduleList = repository.getAllScheduleList(member);

        for(Calendar calendarToJson  : scheduleList ){
            Map<String, Object> map = new HashMap<>();
            map.put("id",calendarToJson.getId());
            map.put("member",calendarToJson.getMember().getUserNumber());
            map.put("title",calendarToJson.getTitle());
            map.put("content",calendarToJson.getContent());
            map.put("schedulePlace",calendarToJson.getScheduleLocation());
            map.put("scheduleStr",calendarToJson.getScheduleStr());
            map.put("scheduleEnd",calendarToJson.getScheduleEnd());
            map.put("alarmTime",calendarToJson.getSetAlarmTime());
            map.put("scheduleTypeDetail",calendarToJson.getScheduleTypeDetail());
            map.put("scheduleType",calendarToJson.getScheduleType());

            jsonArray.add(map);
        };
            paramsMap.put("jsonArray", jsonArray);
    }

    @Override
    public boolean deleteSchedule(Calendar calendar) {

        int count = repository.countConfirmScheduleWriter(calendar);

        System.out.println("count : "+ count);

        if (count == 1 ){
            log.info("----CalendarServiceImpl/deleteSchedule :: if문의 count " + count);
            alarmRepository.deleteAlarm(calendar);
            repository.delete(calendar);
            return true;
        } else {
            return false;
        }

    }
}
