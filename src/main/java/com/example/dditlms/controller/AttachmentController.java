package com.example.dditlms.controller;

import com.example.dditlms.security.JwtSecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
@RequiredArgsConstructor
public class AttachmentController {

    private final JwtSecurityService jwtSecurityService;

    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @RequestParam String token) {
        try{
            String getToken = jwtSecurityService.getToken(token);
        }catch (ExpiredJwtException e){

        }

    }
}
