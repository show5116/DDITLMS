package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.JwtSecurityService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AndroidController {

    private final JwtSecurityService jwtSecurityService;

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    @ResponseBody
    @GetMapping("/android/login")
    public String getLoginData(@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String parse = jwtSecurityService.getToken(token);
        String[] contents = parse.split("&");
        String id = contents[0].split("=")[1];
        Optional<Member> memberWrapper = memberRepository.findByMemberId(id);
        Member member = memberWrapper.orElse(null);
        if(member == null){
            jsonObject.put("answer","fail");
        }else if(passwordEncoder.matches(contents[1].split("=")[1],member.getPassword())){
            jsonObject.put("answer","success");
        } else{
            jsonObject.put("answer","fail");
        }
        return jsonObject.toJSONString();
    }
}
