package com.example.dditlms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequiredArgsConstructor
public class AttachmentController {

    @GetMapping("/download")
    public void download(HttpServletResponse response) {

    }
}
