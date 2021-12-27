package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class scholarshipAndTuitionController {

    @GetMapping("/student/tuitionShow")
    public String tuitionShow(){
        return "";
    }

    @GetMapping("/student/tuitionApplication")
    public String tuitionApplication(){
        return "";
    }

    @GetMapping("/student/scholarshipShow")
    public String scholarshipShow(){
        return "";
    }

    @GetMapping("/student/scholarshipApplication")
    public ModelAndView scholarshipApplication(ModelAndView mav){

        mav.setViewName("/pages/scholarshipApplication");
        return mav;
    }

}
