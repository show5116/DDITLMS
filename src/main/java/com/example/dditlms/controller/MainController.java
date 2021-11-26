package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.IdentificationRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    @PostAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String home(){
        return "pages/index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        @RequestParam(value = "cnt", required = false) String cnt,
                        Model model){
        return "pages/login";
    }

    @GetMapping("/denied")
    public String accessDenied(@RequestParam(value = "exception",required = false) String exception,
                               Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            Member member = ((AccountContext)authentication.getPrincipal()).getMember();
            model.addAttribute("username",member.getMemberId());
        }catch(ClassCastException e){
        }
        model.addAttribute("exception",exception);
        return "pages/denied";
    }

    @GetMapping("/signup")
    public String signup(@RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "exception", required = false) String exception,
                         Model model, MemberForm memberForm){
        return "pages/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("memberForm") MemberForm memberForm){
        return memberService.checkUser(memberForm,passwordEncoder);
    }

    @GetMapping("/forget")
    public String forget(){
        return "pages/forget";
    }

    @GetMapping("/forget/findID")
    public void findID(HttpServletResponse response, @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        String id = memberService.findId(paramMap.get("identification"),paramMap.get("name"));
        if(id==null){
            jsonObject.put("find","false");
        }else{
            jsonObject.put("find","true");
            jsonObject.put("id",id);
        }
        //response.getWriter().print(jsonObject.toJSONString());
    }

    @GetMapping("/student/main")
    public String student(){
        return "/pages/studentmain";
    }
}
