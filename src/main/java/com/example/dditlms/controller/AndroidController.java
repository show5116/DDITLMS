package com.example.dditlms.controller;

import com.example.dditlms.security.JwtSecurityService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AndroidController {

    private final JwtSecurityService jwtSecurityService;

    @ResponseBody
    @GetMapping("/android/login")
    public String getLoginData(@RequestParam String token){
        System.out.println(token);
        System.out.println(jwtSecurityService.getToken(jwtSecurityService.createToken("aaa",60000L)));
        String parse = jwtSecurityService.getToken(token);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("answer",parse);
        return jsonObject.toJSONString();
    }
}
