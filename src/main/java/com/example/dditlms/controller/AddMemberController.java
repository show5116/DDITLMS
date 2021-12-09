package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AddMemberController {

    @GetMapping("/admin/addMember")
    public String addMember(){
        return "/pages/addMember";
    }

}

