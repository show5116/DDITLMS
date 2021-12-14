package com.example.dditlms.controller;

import com.example.dditlms.security.JwtSecurityService;
import com.example.dditlms.util.FileUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AttachmentController {

    private final FileUtil fileUtil;

    @GetMapping("/download")
    public void download(HttpServletResponse response,
                         @RequestParam String token) {
        Map<String,String> map = fileUtil.getToken(token);
        String path = null;
        if(map.get("success").equals("Y")){
            path = map.get("path");
        }else{
            //토큰 인증 만료
        }
        File file = new File(path);
        response.setHeader("Content-Disposition","attachment;filename="+file.getName());
        FileInputStream fileInputStream = null;
        OutputStream out = null;
        try {
            fileInputStream = new FileInputStream(path);
            out = response.getOutputStream();
            int read = 0;
            byte[] buffer = new byte[1024];
            while((read = fileInputStream.read(buffer)) != -1){
                out.write(buffer,0,read);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e){
        } finally {
            try {
                fileInputStream.close();
                out.close();
            } catch (IOException e) {
            } catch (NullPointerException e){
            }
        }
    }
}
