package com.example.dditlms.domain.repository.chat;

import com.example.dditlms.domain.entity.ChatRoom;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.QChatMember;
import com.example.dditlms.domain.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QChatMember.*;
import static com.example.dditlms.domain.entity.QChatRoom.*;

public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<ChatRoom> getChatRoomByMember(Member member) {
        return queryFactory
                .select(chatRoom)
                .from(chatRoom, chatMember)
                .where(chatMember.member.eq(member)
                        , chatRoom.eq(chatMember.chatRoom))
                .orderBy(chatRoom.updateTime.desc())
                .fetch();
    }
}
