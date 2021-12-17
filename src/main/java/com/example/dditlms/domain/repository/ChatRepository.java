package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Chat;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findAllBySelfOrderByChatTimeDesc(Member member);
}
