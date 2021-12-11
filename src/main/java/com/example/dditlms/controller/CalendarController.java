package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar")
    public String calendar(){
        return "pages/calendar-basic";
    }

    @PostMapping("/calendar/add")
    public void addSchedule(HttpServletRequest request , HttpServletResponse response,
                            @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = null;
        CalendarAlarm calendarAlarm = null;

        try {
            calendar = Calendar.builder()
                    .scheduleType(paramMap.get("type"))
                    .scheduleTypeDetail(paramMap.get("typeDetail"))
                    .title(paramMap.get("title"))
                    .content(paramMap.get("content"))
                    .scheduleLocation(paramMap.get("location"))
                    .scheduleStr((paramMap.get("startDate")))
                    .scheduleEnd((paramMap.get("endDate")))
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
        calendarService.addSchedule(calendar);


        jsonObject.put("state","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }


}
