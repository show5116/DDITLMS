package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

    @GetMapping("/signUpSearch")
    public String signUpsearch() {return "pages/signUpSearch";}


}
