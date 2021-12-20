package com.example.dditlms.service;

import com.example.dditlms.domain.dto.EmailDTO;

import java.util.List;

public interface EmailService {
    List<EmailDTO> receiveEmailList(String pop3Host, String storeType, String user, String password);
    EmailDTO viewMail(int messageNumber, List<EmailDTO> emailDTOList);
}
