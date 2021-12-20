package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.chat.ChatMemberRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.NotificationRepository;
import com.example.dditlms.domain.repository.chat.ChatRepository;
import com.example.dditlms.domain.repository.chat.ChatRoomRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.BookmarkService;
import com.example.dditlms.service.ChatService;
import com.example.dditlms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AdditionalController {

    private final BookmarkService bookmarkService;

    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;

    private final MemberRepository memberRepository;

    private final ChatService chatService;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRepository chatRepository;

    @ResponseBody
    @PostMapping("/notification")
    public String getNotification(HttpServletResponse response){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        List<Notification> notificationList = notificationRepository.findAllByMemberOrderByEnterDateDesc(member);
        JSONArray jsonArray = new JSONArray();
        for(Notification notification : notificationList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",notification.getId()+"");
            map.put("time",notification.getEnterDate()+"");
            map.put("title",notification.getName());
            map.put("content",notification.getContent());
            map.put("url",notification.getURL());
            jsonArray.add(map);
        }
        jsonObject.put("notificationList",jsonArray);
        jsonObject.put("success","true");
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/deleteNotification")
    public String deleteNotification(HttpServletResponse response, @RequestParam String id){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        notificationService.deleteNotification(id);
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        List<Notification> notificationList = notificationRepository.findAllByMemberOrderByEnterDateDesc(member);
        JSONArray jsonArray = new JSONArray();
        for(Notification notification : notificationList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",notification.getId()+"");
            map.put("time",notification.getEnterDate()+"");
            map.put("title",notification.getName());
            map.put("content",notification.getContent());
            map.put("url",notification.getURL());
            jsonArray.add(map);
        }
        jsonObject.put("notificationList",jsonArray);
        jsonObject.put("success","true");
        return jsonObject.toJSONString();
    }

    @PostMapping("/getMember")
    @ResponseBody
    public String getMember(HttpServletResponse response,
                            @RequestParam Map<String,String> paramMap){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        Member member =null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        JSONObject jsonObject = new JSONObject();
        String select = paramMap.get("select");
        List<Member> memberList = null;
        if(select.equals("number")){
            memberList =
                    memberRepository.findAllByUserNumberAndMemberIdNotAndMemberIdIsNotNull(Long.parseLong(paramMap.get("data")),member.getMemberId());
        }else if(select.equals("name")){
            memberList =
                    memberRepository.findAllByNameLikeAndMemberIdNotAndMemberIdIsNotNull("%"+paramMap.get("data")+"%",member.getMemberId());
        }
        JSONArray jsonArray = new JSONArray();
        for(Member chatMember : memberList){
            Map<String,String> map = new HashMap<>();
            map.put("number",chatMember.getUserNumber()+"");
            map.put("name",chatMember.getName());
            jsonArray.add(map);
        }
        if(memberList.isEmpty()){
            jsonObject.put("success","false");
            return jsonObject.toJSONString();
        }
        jsonObject.put("success","true");
        jsonObject.put("memberList",jsonArray);
        return jsonObject.toJSONString();
    }

    @PostMapping("/addChat")
    @ResponseBody
    public String addChat(HttpServletResponse response,
                          @RequestBody Map<String,Object> paramMap){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        chatService.addChatRoom(paramMap);
        jsonObject.put("success","true");
        return jsonObject.toJSONString();
    }

    @PostMapping("/getChat")
    @ResponseBody
    public String getChat(HttpServletResponse response){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        List<ChatRoom> chatRooms = chatRoomRepository.getChatRoomByMember(member);
        if(chatRooms.isEmpty()){
            jsonObject.put("success","false");
            return jsonObject.toJSONString();
        }
        JSONArray jsonArray = new JSONArray();
        for(ChatRoom chatRoom :chatRooms){
            Map<String,Object> map = new HashMap<>();
            List<Chat> chatList = chatRepository.findAllByChatRoomOrderByChatTimeDesc(chatRoom);
            JSONArray chatArray= new JSONArray();
            if(chatList.isEmpty()){
                map.put("isEmpty","true");
            }else{
                map.put("isEmpty","false");
                for(Chat chat: chatList){
                    Map<String,Object> chatMap = new HashMap<>();
                    chatMap.put("status",chat.getChatStatus()+"");
                    chatMap.put("time",chat.getChatTime()+"");
                    chatMap.put("content",chat.getContent());
                    chatMap.put("userNumber",chat.getMember().getUserNumber());
                    chatArray.add(chatMap);
                }
            }
            map.put("img",chatRoom.getChatImg());
            map.put("name",chatRoom.getName());
            map.put("id",chatRoom.getId());
            map.put("time",chatRoom.getUpdateTime()+"");
            map.put("chatList",chatArray);
            jsonArray.add(map);
        }
        jsonObject.put("chatRoomList",jsonArray);
        return jsonObject.toJSONString();
    }

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
}
