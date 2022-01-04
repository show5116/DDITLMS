package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.EmailDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.EmailService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    //메일함 첫 페이지
    @GetMapping("/mail")
    public String mail(Model model) {

        //현재 로그인한 사용자 정보(userNumber)를 가져옴
        Member loginMember = MemberUtil.getLoginMember();
        
        String name = loginMember.getName();
        String domain = "@ddit.site";
        String user = (loginMember.getMemberId() + domain);

        model.addAttribute("name", name);
        model.addAttribute("user", user);

        List<EmailDTO> inboxes = emailService.receiveEmailList("INBOX");
        List<EmailDTO> trash = emailService.receiveEmailList("Trash");
        List<EmailDTO> sent = emailService.receiveEmailList("Sent");
        List<EmailDTO> draft = emailService.receiveEmailList("Drafts");
        model.addAttribute("inboxes", inboxes);
        Integer mailCount = inboxes.size();
        Integer trashMailCount = trash.size();
        Integer sentMailCount = sent.size();
        Integer draftMailCount = draft.size();
        model.addAttribute("mailCount", mailCount);
        model.addAttribute("trashMailCount", trashMailCount);
        model.addAttribute("sentMailCount", sentMailCount);
        model.addAttribute("draftMailCount", draftMailCount);

        return "/pages/mailbox";
    }

    //메일 상세보기
    @GetMapping("/mail/mailView")
    public String mailView(Model model, @RequestParam("id") int id, @RequestParam("box") String mailBox) {


        List<EmailDTO> inboxes = emailService.receiveEmailList(mailBox);
        EmailDTO mail = emailService.viewMail((id - 1), inboxes);

        model.addAttribute("mail", mail);
        return "/pages/mailRead";
    }

    //메일 쓰기페이지 이동
    @GetMapping("/mail/write")
    public String writeMailPage(@ModelAttribute EmailDTO dto, HttpServletRequest request, Model model) {

        model.addAttribute("dto", dto);
        return "/pages/mailWrite";
    }

    //메일 쓰기(보내기 + 보낸편지함 저장)
    @PostMapping("/mail/write")
    public String writeMail(@ModelAttribute("dto") EmailDTO dto, HttpServletResponse response) throws IOException {
        EmailDTO emailDTO = dto;
        try {
            emailService.writeMail(emailDTO);
            emailService.sentMailCopy(emailDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>opener.location.reload(); window.close();</script>");
        out.flush();

        return "redirect:/mail";
    }
    
    @GetMapping("/mail/reply/{id}")
    public String replyMailView(Model model,  @PathVariable("id") int id) {
        EmailDTO dto = emailService.replyMailRead(id);
        model.addAttribute("dto", dto);
        return "/pages/mailWrite";
    }

    @PostMapping("/mail/reply")
    public String replyMail(@ModelAttribute("dto") EmailDTO dto, HttpServletRequest request, Model model) {
        EmailDTO emailDTO = dto;
        try {
            emailService.replyMail(emailDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/mail";
    }
    //메일함별 조회
    @GetMapping("/mail/mailBox")
    public String mailBox(Model model, @RequestParam Map<String, Object> param) {

        List<EmailDTO> inboxes = emailService.receiveEmailList(param.get("mailName").toString());
        model.addAttribute("inboxes", inboxes);

        return "/pages/mailbox::#mail";
    }
    //메일 임시 저장
    @PostMapping("/mail/tempMail")
    public String tempMail(@ModelAttribute("dto") EmailDTO dto, Model model, HttpServletResponse response) throws IOException {
        EmailDTO emailDTO = dto;
        try {
            emailService.tempMail(emailDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setContentType("text/html; charset=euc-kr");
        PrintWriter out = response.getWriter();
        out.println("<script>opener.location.reload(); window.close();</script>");
        out.flush();

        return "/mail";
    }

    
    //메일 이동
    @GetMapping("/mail/move")
    public String moveMail(@RequestParam Map<String, Object> param) {
        String mailBox = param.get("mailBox").toString();
        Long id = Long.valueOf(param.get("id").toString());
        String target = param.get("target").toString();
        emailService.moveMail(mailBox, id, target);

        return "/pages/mailbox";
    }

    @GetMapping("/mail/delete")
    public String deleteMail(@RequestParam Map<String, Object> param) {
        String mailBox = param.get("mailBox").toString();
        Long id = Long.valueOf(param.get("id").toString());
        emailService.deleteMail(mailBox, id);

        return "/pages/mailbox";
    }
}
