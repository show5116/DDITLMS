package com.example.dditlms.domain.repository.chat;

import com.example.dditlms.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long>, ChatRoomRepositoryCustom {
    Optional<ChatRoom> findById(Long id);
}
