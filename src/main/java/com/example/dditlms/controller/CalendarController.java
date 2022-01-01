package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Student;
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
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
    private final CalendarService service;
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
    public void addSchedule(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try{member = ((AccountContext)authentication.getPrincipal()).getMember();}  //로그인한 member 정보 저장
        catch(ClassCastException e){}

        /** 파라미터 조회 */
        String alarmTime = (String)paramMap.get("alarmTime");
        String sms = (String)paramMap.get("alarmSms"); // sms알림 설정 여부
        String kakao = (String)paramMap.get("alarmKakao"); // kakao알림 설정 여부
        String scheduleType = (String)paramMap.get("type");
        String scheduleTypeDetail = (String)paramMap.get("typeDetail");
        String title = (String)paramMap.get("title");
        String content = (String)paramMap.get("content");
        String scheduleLocation = (String)paramMap.get("location");
        String scheduleStr = (String)paramMap.get("startDate");
        String scheduleEnd = (String)paramMap.get("endDate");

        /** 파라미터 생생*/
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> map = new HashMap<>();

        /** 서비스 호출 파라미터 구성 */
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("member",member);
        paramsMap.put("alarmTime",alarmTime);
        paramsMap.put("sms",sms);
        paramsMap.put("kakao",kakao);
        paramsMap.put("scheduleType",scheduleType);
        paramsMap.put("scheduleTypeDetail",scheduleTypeDetail);
        paramsMap.put("title",title);
        paramsMap.put("content",content);
        paramsMap.put("scheduleLocation",scheduleLocation);
        paramsMap.put("scheduleStr",scheduleStr);
        paramsMap.put("scheduleEnd",scheduleEnd);
        paramsMap.put("jsonArray", map);

        /** 서비스 호출*/
        service.addSchedule(paramsMap);
        Calendar calendar =(Calendar)paramsMap.get("calendar");
        jsonArray = (JSONArray)paramsMap.get("jsonArray");
        jsonObject.put("state","true");
        jsonObject.put("id",calendar.getId());
        jsonObject.put("list",jsonArray);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/calendar/delete")
    public void deleteSchedule(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
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
        CalendarAlarm calendarAlarm = null;
        Object id = paramMap.get("deleteSchedule");
        Long scheduleId = Long.valueOf(String.valueOf(id));
        try {
            calendar = Calendar.builder()
                    .id(scheduleId)
                    .member(member).build();
        }catch (Exception e){
        }
        boolean result = service.deleteSchedule(calendar);
        if(result ==true){
            jsonObject.put("state","true");
        } else if (result == false){
            jsonObject.put("state","false");
        }
        List<Calendar> scheduleList = calendarRepository.getAllScheduleList(member);
        for (Calendar calendar1 : scheduleList){
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
        }
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

    @PostMapping("/calendar/findAlarmType")
    public void updateSetting(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        String id = (String)paramMap.get("scheduleId");
        String mem = member.getMemberId();

        JSONObject jsonObject = new JSONObject();
        List<String> typeList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String alarmTime = null;
        String typeDetail = null;
        List<String> majorList = new ArrayList<>();

        map.put("scheduleId", id);
        map.put("alarmTime",alarmTime);
        map.put("types",typeList);
        map.put("typeDetail", typeDetail);
        map.put("majorList", majorList);
        service.updateSetting(map);
        jsonObject.put("param",map);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/calendar/updateSchedule")
    public void updateSchedule(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try{member = ((AccountContext)authentication.getPrincipal()).getMember();}  //로그인한 member 정보 저장
        catch(ClassCastException e){}

        /** 파라미터 조회 */
        String scheduleNo = (String)paramMap.get("scheduleNo");
        String type = (String)paramMap.get("type");
        String typeDetaile = (String)paramMap.get("typeDetail");
        String title = (String)paramMap.get("title");
        String content = (String)paramMap.get("content");
        String location = (String)paramMap.get("location");
        String startDate = (String)paramMap.get("startDate");
        String endDate = (String)paramMap.get("endDate");
        String alarmTime = (String)paramMap.get("alarmTime");
        String alarmSMS = (String)paramMap.get("alarmSms");
        String alarmKAKAO = (String)paramMap.get("alarmKakao");

        /** 파라미터 생생*/
        JSONObject jsonObject = new JSONObject();

        /** 서비스 호출 파라미터 구성 */
        Map<String, Object> map = new HashMap<>();
        map.put("member",member);
        map.put("scheduleNo",scheduleNo);
        map.put("type",type);
        map.put("typeDetaile",typeDetaile);
        map.put("title",title);
        map.put("content",content);
        map.put("location",location);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("alarmTime",alarmTime);
        map.put("alarmSMS",alarmSMS);
        map.put("alarmKAKAO",alarmKAKAO);

        /** 서비스 호출*/
        service.updateSchedule(map);
        JSONArray jsonArray = (JSONArray) map.get("jsonArray");
        Long id = (Long) map.get("scheduleId");

        jsonObject.put("state","true");
        jsonObject.put("id", id);
        jsonObject.put("list", jsonArray);
        try{
            response.getWriter().print(jsonObject.toJSONString());
        }catch (IOException e){

        }
    }
}