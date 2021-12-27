package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String scholarshipApplication(){
        return "";
    }

}
