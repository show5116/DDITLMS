package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Chat;

public interface ChatService {
    public void saveChat(Chat chat);
    public void readChat(Chat chat);
    public void deleteChat(Chat chat);
}
