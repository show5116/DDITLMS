package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark")
    public void addBookmark(HttpServletResponse response, HttpServletRequest request,
                            @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        System.out.println(paramMap.get("pathname"));
        Menu menu = Menu.of(paramMap.get("pathname"));
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        bookmarkService.saveBookmark(member,menu);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/mypage")
    public String mypage(){
        return "/pages/mypage";
    }

    @GetMapping("/address")
    public String address(){
        return "/pages/address";
    }
}
