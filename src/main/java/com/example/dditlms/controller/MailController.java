//package com.example.dditlms.controller;
//
//import com.example.dditlms.domain.dto.EmailDTO;
//import com.example.dditlms.domain.entity.Member;
//import com.example.dditlms.security.AccountContext;
//import com.example.dditlms.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class MailController {
//
//    private final EmailService emailService;
//
//    //메일함 첫 페이지
//    @GetMapping("/mail")
//    public String mail(Model model) {
//
//        //현재 로그인한 사용자 정보(userNumber)를 가져옴
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Member member = null;
//        try {
//            member = ((AccountContext) authentication.getPrincipal()).getMember();
//        } catch (ClassCastException e) {
//        }
//        String name = member.getName();
//        String domain = "@ddit.site";
//        String user = (member.getMemberId() + domain);
//
//        model.addAttribute("name", name);
//        model.addAttribute("user", user);
//
//
//        List<EmailDTO> inboxes = emailService.receiveEmailList("INBOX");
//
//        model.addAttribute("inboxes", inboxes);
//        Integer mailCount = inboxes.size();
//        model.addAttribute("mailCount", mailCount);
//
//
//        return "/pages/mailbox";
//    }
//
//    //메일 상세보기
//    @GetMapping("/mailView/{id}")
//    public String mailView(Model model, @PathVariable("id") int id) {
//
//        List<EmailDTO> inboxes = emailService.receiveEmailList("INBOX");
//        EmailDTO mail = emailService.viewMail((id - 1), inboxes);
//
//        model.addAttribute("mail", mail);
//
//        return "/pages/mailRead";
//    }
//
//    //메일 쓰기페이지 이동
//    @GetMapping("/writeMail")
//    public String writeMailPage(@ModelAttribute EmailDTO dto, HttpServletRequest request, Model model) {
//
//        model.addAttribute("dto", dto);
//        return "/pages/mailWrite";
//    }
//
//    //메일 쓰기(보내기)
//    @PostMapping("/writeMail")
//    public String writeMail(@ModelAttribute("dto") EmailDTO dto, HttpServletRequest request, Model model) {
//
//
//        EmailDTO emailDTO = dto;
//        emailService.writeMail(emailDTO);
//
//        return "redirect:/mail";
//    }
//}
