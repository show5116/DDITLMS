package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PreCourseRegistController {

    @GetMapping("/preCourseRegistration")
    public String preliminaryCourseRegistration(){ return "pages/preCourseRegistration";}


}
