package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.CalendarAlarm;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

    private final CalendarService calendarService;
    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/calendar")
    public ModelAndView calendar(ModelAndView mav){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        String type = "MAJOR";

        List<Calendar> scheduleList = calendarRepository.getAllScheduleList (member, type);

        for (Calendar calendar :
                scheduleList) {
            log.info("====scheduleList : " + calendar.getTitle());
        }

        mav.addObject("scheduleList",scheduleList);
        mav.setViewName("pages/calendar-basic");

        return mav;
    }

   /*
    @GetMapping("/testCalendar")
    @ResponseBody
//    public  List<Calendar> testCalendar(){
    public  List<Tuple>  testCalendar(){

        Optional<Member> userNumber = memberRepository.findByUserNumber(14132131L);



        String type = "major";

        List<Tuple>  scheduleList = calendarRepository.getAllScheduleList(userNumber.get(), type);
//        Long scheduleList = calendarRepository.getAllScheduleList(userNumber.get(), type);

        log.info("---------------userNumber : " + userNumber);
        log.info("___________________________" + scheduleList);


        return scheduleList;

    };
    */

    public void deleteSchedule(HttpServletRequest request, HttpServletResponse response, Integer scheduleId){
        JSONObject jsonObject = new JSONObject();
        Member member = null;

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
        calendar = calendarService.addSchedule(calendar);


        jsonObject.put("state","true");
        jsonObject.put("id",calendar.getId());
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }



}
