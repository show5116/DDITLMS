package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.ChatStatus;
import com.example.dditlms.domain.entity.Chat;
import com.example.dditlms.domain.entity.ChatMember;
import com.example.dditlms.domain.entity.ChatRoom;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.chat.ChatMemberRepository;
import com.example.dditlms.domain.repository.chat.ChatRepository;
import com.example.dditlms.domain.repository.chat.ChatRoomRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MemberRepository memberRepository;

    private final ChatRepository chatRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMemberRepository chatMemberRepository;

    @Override
    public void addChatRoom(Map<String, Object> map) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(map.get("name")+"")
                .updateTime(new Date())
                .chatImg(map.get("img")+"").build();
        chatRoomRepository.save(chatRoom);
        List<Map<String,Object>> memberList = (List<Map<String,Object>>)map.get("list");
        for(Map<String,Object> memberMap : memberList){
            Optional<Member> memberWrapper = memberRepository.findByUserNumber(Long.parseLong(memberMap.get("id")+""));
            Member member = memberWrapper.orElse(null);
            ChatMember chatMember = ChatMember.builder()
                    .chatRoom(chatRoom)
                    .member(member).build();
            chatMemberRepository.save(chatMember);
        }
        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        ChatMember chatMember = ChatMember.builder()
                .chatRoom(chatRoom)
                .member(member).build();
        chatMemberRepository.save(chatMember);
    }

    @Override
    public void saveChat(Chat chat){
        chatRepository.save(chat);
    }

    @Override
    public void readChat(Chat chat){
        chat.setChatStatus(ChatStatus.READ);
        chatRepository.save(chat);
    }

    @Override
    public void deleteChat(Chat chat){
        chat.setChatStatus(ChatStatus.DELETED);
        chatRepository.save(chat);
    }
}
