package com.example.dditlms.domain.repository.chat;

import com.example.dditlms.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>, ChatRoomRepositoryCustom {

}
