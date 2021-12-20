package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Chat;

import java.util.Map;

public interface ChatService {
    public void addChatRoom(Map<String,Object> map);
    public void saveChat(Chat chat);
    public void readChat(Chat chat);
    public void deleteChat(Chat chat);
}
