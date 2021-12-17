package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;
    private final CalendarRepository calendarRepository;




    @GetMapping("/calendar")
    public ModelAndView calendar(ModelAndView mav){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }

        List<Calendar> scheduleList = calendarRepository.getAllScheduleList (member);

        mav.addObject("scheduleList",scheduleList);
        mav.setViewName("pages/calendar-basic");

        return mav;
    }

    @PostMapping("/calendar/add")
    public void addSchedule(HttpServletRequest request , HttpServletResponse response,
                            @RequestParam Map<String,String> paramMap){
        log.info("---------------------addSchedule");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Calendar calendar = null;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        CalendarAlarm calendarAlarmSMS = null;
        CalendarAlarm calendarAlarmKakako = null;
        List<String> alarmType = new ArrayList<>();
        Date date = null;
        String scheduleStart = null;
        String sms = null;
        String kakao = null;
        Long scheduleMaxId =0L;

        String startDate = paramMap.get("startDate");
        String alarmTime = paramMap.get("alarmTime");
        int alarmCount = Integer.parseInt(alarmTime);

        // 알림설정 없이 그냥 일정만 등록
        try {
            calendar = Calendar.builder()
                    .scheduleType(paramMap.get("type"))
                    .scheduleTypeDetail(paramMap.get("typeDetail"))
                    .title(paramMap.get("title"))
                    .content(paramMap.get("content"))
                    .scheduleLocation(paramMap.get("location"))
                    .scheduleStr(paramMap.get("startDate"))
                    .scheduleEnd(paramMap.get("endDate"))
                    .setAlarmTime(paramMap.get("alarmTime"))
                    .content(paramMap.get("text"))
                    .member(member).build();

        }catch (Exception e){
        }
        //서비스로 save 호출
        log.info("---------------------일정등록 서비스 시작");
        calendar = calendarService.addSchedule(calendar);
        log.info("---------------------일정등록 서비스 끝");

        try {
            date = df.parse(startDate);
        }catch (ParseException e){
        }
        cal.setTime(date);

        if (alarmCount > 0){
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
            sms = paramMap.get("alarmSms");
            kakao = paramMap.get("alarmKakao");
            Long maxId = calendarRepository.getLastScheduleNumber();
            scheduleMaxId = maxId;
        }

        try {
            calendarAlarmSMS = calendarAlarmSMS.builder()
                    .id(scheduleMaxId)
                    .scheduleContent(paramMap.get("title"))
                    .scheduleAlarmTime(scheduleStart)
                    .scheduleAlarmType("SMS")
                    .member(member).build();
            calendarAlarmKakako = calendarAlarmKakako.builder()
                    .id(scheduleMaxId)
                    .scheduleContent(paramMap.get("title"))
                    .scheduleAlarmTime(scheduleStart)
                    .scheduleAlarmType("KAKAO")
                    .member(member).build();
        }catch (Exception e){

        }
        log.info("---------------------if문 들어가기전 sms :" + sms);
        log.info(sms.equals("true") + "<<<<");
        if(sms.equals("true")){
            log.info("----------------alarmSMS insert 시작");
            calendarService.addAlarm(calendarAlarmSMS);
            log.info("----------------alarmSMS insert 성공");
        }
        if(kakao.equals("true")){
            calendarService.addAlarm(calendarAlarmKakako);
            log.info("----------------alarmKAKAO insert 성공");
        }



        List<Calendar> scheduleList = calendarRepository.getAllScheduleList(member);

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

        jsonObject.put("state","true");
        jsonObject.put("id",calendar.getId());
        jsonObject.put("list",jsonArray);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }


    }

    @PostMapping("/calendar/delete")
    public void deleteSchedule(HttpServletRequest request , HttpServletResponse response,
                               @RequestParam Map<String, Object> paramMap){

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        Calendar calendar = null;
        Object id = paramMap.get("deleteSchedule");
        Long scheduleId = Long.valueOf(String.valueOf(id));
        try {
            calendar = Calendar.builder()
                    .id(scheduleId)
                    .member(member).build();

        }catch (Exception e){
        }

        boolean result = calendarService.deleteSchedule(calendar);

        if(result ==true){
            log.info("---------------------deleteSchedule SUCCESS");
            jsonObject.put("state","true");
        } else if (result == false){
            log.info("---------------------deleteSchedule FAILED");
            jsonObject.put("state","false");
        }

        List<Calendar> scheduleList = calendarRepository.getAllScheduleList(member);

        for (Calendar calendar1 : scheduleList){
            log.info("-------------"+calendar1.getTitle());
        }

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

        jsonObject.put("list",jsonArray);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }

    }

    @GetMapping("/calendar/memberInfo")
    public void getMyInfo(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        JSONObject jsonObject = new JSONObject();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }

        Role role = member.getRole();
        log.info("--------Role:" + role);
        String myRole = role +"";

        jsonObject.put("myRole", myRole);

        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/calendar/getMajor")
    public void getMajor(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        JSONObject jsonObject = new JSONObject();

        List<String> majorList = calendarRepository.getAllMajorList();

        jsonObject.put("getMajorList",majorList);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }






























}































