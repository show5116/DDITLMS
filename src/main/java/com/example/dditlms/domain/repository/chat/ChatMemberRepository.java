package com.example.dditlms.domain.repository.chat;

import com.example.dditlms.domain.entity.ChatMember;
import com.example.dditlms.domain.entity.ChatRoom;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember,Long> {
    List<ChatMember> findAllByChatRoom(ChatRoom chatRoom);
}
