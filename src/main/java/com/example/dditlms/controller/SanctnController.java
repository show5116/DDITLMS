package com.example.dditlms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanctnController {

    @GetMapping("/sanctn")
    public String calendar(){
        return "/pages/sanction";
    }
}
