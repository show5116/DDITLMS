package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.MemberDetail;
import com.example.dditlms.domain.repository.MemberDetailRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.BookmarkService;
import com.example.dditlms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MypageController {

    private final BookmarkService bookmarkService;

    private final MemberService memberService;

    private final MemberDetailRepository memberDetailRepository;

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
        HttpSession session = request.getSession();
        Set<Bookmark> bookmarks = bookmarkService.getBookmarks(member);
        session.setAttribute("bookmarks",bookmarks);
        jsonObject.put("state","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @GetMapping("/mypage")
    public ModelAndView mypage(ModelAndView mav){
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        mav.setViewName("/pages/mypage");
        mav.addObject(member);
        return mav;
    }

    @PostMapping("/mypage/changeMain")
    public void changeMainInformation(HttpServletResponse response, HttpServletRequest request,
                               @RequestParam Map<String,String> paramMap) {
        JSONObject jsonObject = new JSONObject();
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        member.setEmail(paramMap.get("email"));
        member.setPhone(paramMap.get("phone"));
        memberService.changeData(member);
        jsonObject.put("change","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/mypage/changeDetail")
    public void changeDetailInformation(HttpServletResponse response, HttpServletRequest request,
                                      @RequestParam Map<String,String> paramMap) {
        JSONObject jsonObject = new JSONObject();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        Optional<MemberDetail> memberDetailWrapper = memberDetailRepository.findByMember(member);
        MemberDetail memberDetail = memberDetailWrapper.orElse(null);
        if(memberDetail == null){
            memberDetail = new MemberDetail();
        }
        memberDetail.setBank(paramMap.get("bank"));
        memberDetail.setAcnutNo(paramMap.get("acountNo"));
        memberDetail.setZip(paramMap.get("zip"));
        memberDetail.setRdnmAdr(paramMap.get("roadNameAddr"));
        memberDetail.setLnmAdr(paramMap.get("longNameAddr"));
        memberDetail.setDetailAdr(paramMap.get("detailAddr"));
        memberDetail.setRefer(paramMap.get("refer"));
        memberDetail.setSelfIntr(paramMap.get("selfIntr"));
        memberDetail.setMember(member);
        member.setMemberDetail(memberDetail);
        memberService.changeData(member);
        jsonObject.put("change","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }

    @PostMapping("/mypage/chageImg")
    public void changeImg(HttpServletResponse response, HttpServletRequest request,
                                        @RequestParam Map<String,String> paramMap) {
        JSONObject jsonObject = new JSONObject();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        member.setMemberImg(paramMap.get("img"));
        memberService.changeData(member);
        HttpSession session = request.getSession();
        session.setAttribute("memberImg",member.getMemberImg());
        jsonObject.put("change","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }
        @GetMapping("/address")
    public String address(){
        return "/pages/address";
    }
}
