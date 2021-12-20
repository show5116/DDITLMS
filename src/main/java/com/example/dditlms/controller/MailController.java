package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.EmailDTO;
import com.example.dditlms.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
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

        List<EmailDTO> inboxes = emailService.receiveEmailList("mail.ddit.site", "pop3", "test2@ddit.site", "test123");

        log.info(String.valueOf(inboxes));
        model.addAttribute("inboxes", inboxes);


        return "/pages/mailbox";
    }

    @GetMapping("/mailView")
    public String mailView(Model model) {

        Object inboxes = model.getAttribute("inboxes");

        log.info((String) inboxes);


        return "/pages/mailbox";
    }


}
