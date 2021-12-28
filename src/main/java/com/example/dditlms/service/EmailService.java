package com.example.dditlms.service;

import com.example.dditlms.domain.dto.EmailDTO;

import java.io.IOException;
import java.util.List;

public interface EmailService {
    List<EmailDTO> receiveEmailList(String folderName);
    EmailDTO viewMail(int messageNumber, List<EmailDTO> emailDTOList);
    void writeMail(EmailDTO emailDTO) throws IOException;
    EmailDTO replyMailRead(int id);
    void replyMail(EmailDTO emailDTO) throws IOException;
    void testCreateBox();
    void moveMail(String folderName, int messageNumber);
    void moveMailTest(String folderName, Long mailUID);
    void sentMailCopy();
    void tempMail(EmailDTO emailDTO) throws IOException;
}
