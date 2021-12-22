package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    @GetMapping("/signUpSearch")
    public String signUpsearch() {return "pages/signUpSearch";}

    @GetMapping("/preCourseRegistration")
    public String preliminaryCourseRegistration(){ return "pages/preCourseRegistration";}

    @GetMapping("/courseRegistration")
    public String courseRegistration() {return "pages/courseRegistration";}




}
