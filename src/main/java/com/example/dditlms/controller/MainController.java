package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.MailDTO;
import com.example.dditlms.domain.dto.SMSDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.IdentificationRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.MailService;
import com.example.dditlms.service.MemberService;
import com.example.dditlms.service.SMSService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final SMSService smsService;

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
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/forget/mail")
    public void changePasswordMail(HttpServletResponse response, @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        String id = memberService.findId(paramMap.get("identification"),paramMap.get("name"));
        if(id==null){
            jsonObject.put("find","false");
        }else{
            MailDTO mailDTO = new MailDTO(paramMap.get("mail"),"제목","니 비번");
            mailService.mailSend(mailDTO);
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/forget/phone")
    public void chanePasswordPhone(HttpServletResponse response, @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        String id = memberService.findId(paramMap.get("identification"),paramMap.get("name"));
        SMSDTO smsdto = new SMSDTO("01067856542","01051161830","SMS","문자요","test app 1.2");
        smsService.SendMessage(smsdto);
        if(id==null){
            jsonObject.put("find","false");
        }else{
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/fileUpload")
    public String fileUpload(){
        return "/pages/dropzonetest";
    }

    @PostMapping("/upload")
    public String fileUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                             MultipartHttpServletRequest request) {
        Map<String, MultipartFile> map = request.getFileMap();
        return "/pages/dropzonetest";
    }


    @GetMapping("/student/main")
    public String student(){
        return "/pages/studentmain";
    }
}
