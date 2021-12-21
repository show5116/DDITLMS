package com.example.dditlms.websocket;

import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.chat.ChatMemberRepository;
import com.example.dditlms.domain.repository.chat.ChatRoomRepository;
import com.example.dditlms.service.ChatService;
import com.example.dditlms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final MemberRepository memberRepository;

    private final NotificationService notificationService;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMemberRepository chatMemberRepository;

    private final ChatService chatService;

    private static Map<Long,WebSocketSession> map = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String payload = new String(Base64.getDecoder().decode(message.getPayload()));
        String decodePayload = URLDecoder.decode(payload);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(decodePayload);
        String command = (String)jsonObject.get("command");
        if(command.equals("notice")){
            sendNotification(session,jsonObject,message);
        }else if(command.equals("chat")){
            sendChat(session,jsonObject,message);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        Optional<Member> memberWrapper = memberRepository.findByMemberId(session.getPrincipal().getName());
        Member member = memberWrapper.orElse(null);
        map.put(member.getUserNumber(),session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        Optional<Member> memberWrapper = memberRepository.findByMemberId(session.getPrincipal().getName());
        Member member = memberWrapper.orElse(null);
        map.remove(member.getUserNumber());
    }

    private void sendNotification(WebSocketSession session,JSONObject jsonObject,TextMessage message){
        JSONArray targets = (JSONArray)jsonObject.get("targets");
        Iterator targetIterator = targets.iterator();
        List<Notification> notificationList = new ArrayList<>();
        while(targetIterator.hasNext()){
            try {
                Optional<Member> targetMemberWrapper = memberRepository.findByUserNumber(Long.parseLong(targetIterator.next()+""));
                Member targetMember = targetMemberWrapper.orElse(null);
                Notification notification = Notification.builder()
                        .enterDate(new Date())
                        .name(jsonObject.get("title")+"")
                        .content(jsonObject.get("message")+"")
                        .URL(jsonObject.get("url")+"")
                        .delete('N')
                        .member(targetMember).build();
                notificationList.add(notification);
                try{
                    map.get(targetMember.getUserNumber()).sendMessage(message);
                }catch (NullPointerException e){
                }
            } catch (Exception e) {
            }
        }
        notificationService.saveNotifications(notificationList);
    }

    private void sendChat(WebSocketSession session,JSONObject jsonObject,TextMessage message){
        Long targetId = Long.parseLong(jsonObject.get("target")+"");
        Optional<ChatRoom> chatRoomWrapper = chatRoomRepository.findById(targetId);
        ChatRoom chatRoom = chatRoomWrapper.orElse(null);
        Optional<Member> selfWrapper = memberRepository.findByMemberId(session.getPrincipal().getName());
        Member self = selfWrapper.orElse(null);
        Chat chat = Chat.builder()
                .content(jsonObject.get("message")+"")
                .member(self)
                .chatRoom(chatRoom)
                .chatTime(new Date()).build();
        chatService.saveChat(chat);
        List<ChatMember> chatMemberList = chatMemberRepository.findAllByChatRoom(chatRoom);
        for(ChatMember chatMember : chatMemberList){
            if(map.get(chatMember.getMember().getUserNumber())!=null){
                System.out.println(chatMember.getMember().getUserNumber());
                try{
                    map.get(chatMember.getMember().getUserNumber()).sendMessage(message);
                }catch (IOException e){
                }
            }
        }
    }
}
