package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class StudentDepController {

    @GetMapping("/studentDep/register")
    public ModelAndView registerAdmin(ModelAndView mav){

        mav.setViewName("/pages/registerAdmin");
        return mav;
    }

    @GetMapping("/studentDep/studentAssign")
    public ModelAndView studentAssign(ModelAndView mav){


        return mav;
    }
}
