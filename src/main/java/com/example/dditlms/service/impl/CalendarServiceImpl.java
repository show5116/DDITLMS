package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.CalendarAlarmRepository;
import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository repository;
    private final CalendarAlarmRepository alarmRepository;

    @Override
    @Transactional
    public void addSchedule(@NotNull Map<String, Object> paramsMap) {

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
        String scheduleStr = (String)paramsMap.get("scheduleStr");
        String scheduleEnd = (String)paramsMap.get("scheduleEnd");

        log.info("---------addschedule params --------------- ");
        log.info(member.getName());
        log.info(alarmTime);
        log.info(sms);
        log.info(kakao);
        log.info(type);
        log.info(typeDetail);
        log.info(title);
        log.info(content);
        log.info(place);
        log.info(scheduleStr);
        log.info(scheduleEnd);
        log.info("---------------------------------------------------- ");

        /** 파라미터 생성 */
        JSONArray jsonArray = new JSONArray();
        Calendar calendar = null;
        CalendarAlarm calendarAlarmSMS = null;
        CalendarAlarm calendarAlarmKakako = null;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        String scheduleStart = ""; //알람시간

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
                log.info("-----service에서 sms schedule 등록");
                calendar = repository.save(calendar);
                paramsMap.put("calendar", calendar);
                alarmRepository.save(calendarAlarmSMS);
                log.info("-----service에서 sms schedule 등록 성공");
            }
            if(kakao.equals("true")){
                log.info("-----service에서 kakao schedule 등록");
                calendar = repository.save(calendar);
                paramsMap.put("calendar", calendar);
                alarmRepository.save(calendarAlarmKakako);
                log.info("-----service에서 kakao schedule 등록 성공");
            }
        } else{
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
            }catch (Exception e){
            }

            log.info("-----service에서 schedule 등록");
            calendar = repository.save(calendar);
            paramsMap.put("calendar", calendar);
            log.info("-----service에서 schedule 등록 성공");
        }

        log.info("-----service에서 scheduleList 조회");
        List<Calendar> scheduleList = repository.getAllScheduleList(member);
        log.info("-----service에서 scheduleList 조회 성공" );

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
        CalendarAlarm calendarAlarm = new CalendarAlarm();
        int count = repository.countConfirmScheduleWriter(calendar);

        System.out.println("count : "+ count);

        if (count == 1 ){
            log.info("----CalendarServiceImpl/deleteSchedule :: if문의 count " + count);
            List<Long> id = alarmRepository.getAlarmId(calendar);
            for (Long alarmId : id){
                try {
                    calendarAlarm = CalendarAlarm.builder()
                            .id(alarmId)
                            .build();
                }catch (Exception e){}
                log.info("----CalendarServiceImpl/deleteSchedule :: if문의 alarmID" + id);
                log.info("-----alarm 삭제 ");
                alarmRepository.delete(calendarAlarm);
                log.info("-----alarm 삭제 성공");
            }
            log.info("-----schedule 삭제 ");
            repository.delete(calendar);
            log.info("-----schedule 삭제 성공");
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void updateSetting(Map<String, Object> map){

        String id = (String)map.get("scheduleId");
        Long scheduleId = Long.parseLong(id);
        log.info("-----service-findAlarmType :: scheduleId = " + scheduleId);
        List<String> types = new ArrayList<>();
        Calendar calendar = new Calendar();

        calendar = repository.getSchedule(scheduleId);      //수정하려는 일정 상세내용
        List<String> major = repository.getAllMajorList();  //학과 리스트
        String time = calendar.getSetAlarmTime();
        String typeDetail = calendar.getScheduleTypeDetail();
        log.info("-----service-findAlarmType :: calendar = {}",calendar);
        log.info("-----service-findAlarmType :: time = {}",time);
        log.info("-----service-findAlarmType :: typeDetail = {}",typeDetail);
        map.put("alarmTime",time);
        map.put("typeDetail", typeDetail);
        map.put("majorList", major);

        if (!time.equals("none")){
            types = alarmRepository.findAlarmType(scheduleId);  //알림 유형 조회
            log.info("-----service-findAlarmType :: types = {}",types);
            map.put("types",types);
        }
    }

    @Override
    @Transactional
    public void updateSchedule(Map<String, Object> map) {

        /** 파라미터 조회 ***************************************************************************/
        log.info("-----updateService-파라미터 조회");
        Member member = (Member) map.get("member");
        String scheduleNo = (String) map.get("scheduleNo");
        String type = (String) map.get("type");
        String typeDetail = (String) map.get("typeDetail");
        String title = (String) map.get("title");
        String content = (String) map.get("content");
        String location = (String) map.get("location");
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");
        String alarmTime = (String) map.get("alarmTime");
        String alarmSMS = (String) map.get("alarmSMS");
        String alarmKAKAO = (String) map.get("alarmKAKAO");
        Long id = Long.parseLong(scheduleNo);

        map.put("scheduleId", id);

        log.info("---------updateSchedule params --------------- ");
        log.info(member.getName());
        log.info(scheduleNo);
        log.info(type);
        log.info(typeDetail);
        log.info(title);
        log.info(content);
        log.info(location);
        log.info(startDate);
        log.info(endDate);
        log.info(alarmTime);
        log.info(alarmSMS);
        log.info(alarmKAKAO);
        log.info("---------------------------------------------------- ");


        /** 파라미터 생성 */
        log.info("-----updateService-파라미터 생성");
        JSONArray jsonArray = new JSONArray();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        String scheduleStart = ""; //알람시간

        try {date = df.parse(startDate);}catch (ParseException e){}
        cal.setTime(date);

        if (!alarmTime.equals("none")){
            log.info("-----updateService-alarmTime이 none이 아닐때 if문");
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

            List<CalendarAlarm> calendarAlarmList = alarmRepository.getAlarmSchedule(id);
            Optional<Calendar> byId = repository.findById(id);
            Calendar findCaledar = byId.get();
            findCaledar.setScheduleType(type);
            findCaledar.setScheduleTypeDetail(typeDetail);
            findCaledar.setTitle(title);
            findCaledar.setContent(content);
            findCaledar.setScheduleLocation(location);
            findCaledar.setScheduleStr(startDate);
            findCaledar.setScheduleEnd(endDate);
            findCaledar.setSetAlarmTime(alarmTime);
            repository.save(findCaledar);
            for (CalendarAlarm alarm: calendarAlarmList ) {
                if(alarmSMS.equals("true")) {
                    String getAlarmType = alarm.getScheduleAlarmType();
                    if(getAlarmType.equals("SMS")){
                        Optional<CalendarAlarm> alarmById1 = alarmRepository.findById(alarm.getId());
                        CalendarAlarm calendarAlarm = alarmById1.get();
                        calendarAlarm.setScheduleContent(title);
                        calendarAlarm.setScheduleAlarmTime(scheduleStart);
                        calendarAlarm.setScheduleAlarmType("SMS");
                        alarmRepository.save(calendarAlarm);
                    }
                }
                if(alarmKAKAO.equals("true")) {
                    String getAlarmType = alarm.getScheduleAlarmType();
                    if(getAlarmType.equals("KAKAO")){
                        Optional<CalendarAlarm> alarmById1 = alarmRepository.findById(alarm.getId());
                        CalendarAlarm calendarAlarm = alarmById1.get();
                        calendarAlarm.setScheduleContent(title);
                        calendarAlarm.setScheduleAlarmTime(scheduleStart);
                        calendarAlarm.setScheduleAlarmType("KAKAO");
                        alarmRepository.save(calendarAlarm);
                    }
                }
            }
        } else{
            Optional<Calendar> byId = repository.findById(id);
            Calendar findCaledar = byId.get();
            findCaledar.setScheduleType(type);
            findCaledar.setScheduleTypeDetail(typeDetail);
            findCaledar.setTitle(title);
            findCaledar.setContent(content);
            findCaledar.setScheduleLocation(location);
            findCaledar.setScheduleStr(startDate);
            findCaledar.setScheduleEnd(endDate);
            findCaledar.setSetAlarmTime(alarmTime);
            repository.save(findCaledar);
        }

        List<Calendar> scheduleList = repository.getAllScheduleList(member);
        log.info("-----updateService The end :: scheduleList = {}", scheduleList);

        for(Calendar calendarToJson  : scheduleList ){
            log.info("-----updateService The end :: schedule = {}", calendarToJson);
            Map<String, Object> listMap = new HashMap<>();
            listMap.put("id",calendarToJson.getId());
            listMap.put("member",calendarToJson.getMember().getUserNumber());
            listMap.put("title",calendarToJson.getTitle());
            listMap.put("content",calendarToJson.getContent());
            listMap.put("schedulePlace",calendarToJson.getScheduleLocation());
            listMap.put("scheduleStr",calendarToJson.getScheduleStr());
            listMap.put("scheduleEnd",calendarToJson.getScheduleEnd());
            listMap.put("alarmTime",calendarToJson.getSetAlarmTime());
            listMap.put("scheduleTypeDetail",calendarToJson.getScheduleTypeDetail());
            listMap.put("scheduleType",calendarToJson.getScheduleType());

            jsonArray.add(listMap);
        };
        log.info(jsonArray.toString());
            map.put("jsonArray", jsonArray);
            log.info("-----updateService-update 끝");
    }

















    }




































