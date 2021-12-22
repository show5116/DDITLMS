package com.example.dditlms.domain.repository.chat;

import com.example.dditlms.domain.entity.ChatRoom;
import com.example.dditlms.domain.entity.Member;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> getChatRoomByMember(Member member);
}
