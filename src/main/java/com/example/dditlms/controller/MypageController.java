package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final BookmarkService bookmarkService;

    @PostMapping("/bookmark")
    public void addBookmark(HttpServletResponse response, HttpServletRequest request,
                            @RequestParam Map<String,String> paramMap){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonlist = new JSONArray();
        Menu menu = Menu.of(paramMap.get("pathname"));
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        bookmarkService.saveBookmark(member,menu);
        HttpSession session = request.getSession();
        Set<Bookmark> bookmarks = bookmarkService.getBookmarks(member);
        session.setAttribute("bookmarks",bookmarks);
        bookmarks.forEach(bookmark -> {
            Map<String,String> map = new HashMap<String,String>();
            map.put("icon",bookmark.getMenu().getIcon());
            map.put("url",bookmark.getMenu().getUrl());
            map.put("name",bookmark.getMenu().getName());
            jsonlist.add(map);
        });
        jsonObject.put("state","true");
        jsonObject.put("bookmarks",jsonlist);
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/bookmark/remove")
    public void removeBookmark(HttpServletResponse response, HttpServletRequest request,
                               @RequestParam Map<String,String> paramMap){
        JSONObject jsonObject = new JSONObject();
        Menu menu = Menu.of(paramMap.get("pathname"));
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        bookmarkService.removeBookmark(member,menu);
        jsonObject.put("state","true");
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
