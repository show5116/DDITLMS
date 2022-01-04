package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.dto.MailDTO;
import com.example.dditlms.domain.dto.SMSDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.security.CustomAuthenticationSuccessHandler;
import com.example.dditlms.security.JwtSecurityService;
import com.example.dditlms.service.MailService;
import com.example.dditlms.service.MemberService;
import com.example.dditlms.service.SMSService;
import com.example.dditlms.util.MemberUtil;
import com.example.dditlms.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final SMSService smsService;

    private final OtpUtil otpUtil;

    private final JwtSecurityService jwtSecurityService;

    private final CustomAuthenticationSuccessHandler successHandler;

    @GetMapping("/")
    public String home(HttpServletRequest request,
                       HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpSession session = request.getSession();
        try {
            Member member = ((AccountContext)authentication.getPrincipal()).getMember();
            if(session.getAttribute("memberImg") == null){
                successHandler.onAuthenticationSuccess(request,response,authentication);
            }
        }catch(ClassCastException e){
            return "redirect: /login";
        } catch (IOException e) {
        } catch (ServletException e) {
        }
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
            jsonObject.put("find","true");
            String[] otp = otpUtil.getOTP();
            MailDTO mailDTO = new MailDTO(paramMap.get("mail"),"제목",otp[1]);
            mailService.mailSend(mailDTO);
            Cookie cookie = new Cookie("otp",passwordEncoder.encode(otp[0]));
            cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/forget/phone")
    public void chanePasswordPhone(HttpServletResponse response, HttpServletRequest request,
                                   @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        String id = memberService.findId(paramMap.get("identification"),paramMap.get("name"));
        if(id==null || id.equals(paramMap.get(id))){
            jsonObject.put("find","false");
        }else{
            jsonObject.put("find","true");
            String[] otp = otpUtil.getOTP();
            SMSDTO smsdto = new SMSDTO(paramMap.get("phone"),"01051161830","SMS",otp[1],"test app 1.2");
            smsService.SendMessage(smsdto);
            Cookie cookie = new Cookie("otp",passwordEncoder.encode(otp[0]));
            cookie.setMaxAge(60 * 5);
            response.addCookie(cookie);
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/forget/otpcheck")
    public void otpCheck(HttpServletResponse response, HttpServletRequest request,
                         @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();

        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if(cookies !=null){
            for(Cookie findcookie : cookies){
                if(findcookie.getName().equals("otp")){
                    cookie = findcookie;
                }
            }
        }
        String otp = paramMap.get("otpText");
        if(passwordEncoder.matches(otp,cookie.getValue())){
            jsonObject.put("check","true");
        }else{
            jsonObject.put("check","false");
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/forget/changepassword")
    public void changepassword(HttpServletResponse response, HttpServletRequest request,
                               @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        if(memberService.changePW(paramMap.get("id"),passwordEncoder.encode(paramMap.get("password")))){
            jsonObject.put("change","true");
        }else{
            jsonObject.put("change","false");
        }
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @ResponseBody
    @PostMapping("/ssotoken")
    public String madeSsoToken(){
        JSONObject jsonObject = new JSONObject();
        String id = MemberUtil.getLoginMember().getMemberId();
        String token = jwtSecurityService.createToken(id,60000L);
        jsonObject.put("token",token);
        return jsonObject.toJSONString();
    }
}
