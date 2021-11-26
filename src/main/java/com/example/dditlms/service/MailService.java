package com.example.dditlms.service;

import com.example.dditlms.domain.dto.MailDTO;

public interface MailService {
    public void mailSend(MailDTO mailDTO);
}
