package com.example.dditlms.controller;

import com.example.dditlms.DditlmsApplication;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        JSONObject jsonObject = new JSONObject();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        Calendar calendar = null;
        CalendarAlarm calendarAlarm = null;
        JSONArray jsonArray = new JSONArray();

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
            calendarAlarm = calendarAlarm.builder()
                    .scheduleAlarmTime(paramMap.get("alarmTime"))
                    .scheduleAlarmType(paramMap.get("alarmSms"))
                    .member(member).build();

        }catch (Exception e){
        }
        //서비스로 save 호출
        calendar = calendarService.addSchedule(calendar);

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































