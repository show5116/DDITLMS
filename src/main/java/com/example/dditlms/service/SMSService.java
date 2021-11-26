package com.example.dditlms.service;

import com.example.dditlms.domain.dto.SMSDTO;

public interface SMSService {
    public void SendMessage(SMSDTO smsdto);
}
