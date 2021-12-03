package com.example.dditlms.controller;

import com.example.dditlms.domain.repository.CalendarRepository;
import com.example.dditlms.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar")
    public String calendar(){
        return "/pages/calendar-basic";
    }


}
