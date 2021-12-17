package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.ChatStatus;
import com.example.dditlms.domain.entity.Chat;
import com.example.dditlms.domain.repository.ChatRepository;
import com.example.dditlms.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

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
