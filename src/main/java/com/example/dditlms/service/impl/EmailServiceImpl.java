package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.EmailDTO;
import com.example.dditlms.service.EmailService;
import com.sun.mail.pop3.POP3Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
@Transactional(readOnly = true)
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public List<EmailDTO> receiveEmailList(String pop3Host, String storeType, String user, String password) {

        try {
            //1) 세션값 받아옴, 메일 수신은 pop3프로토콜로만 받기로 함.
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            Session emailSession = Session.getDefaultInstance(properties);

            //2) pop3서버로 연결 함.
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);

            //3) 받은 메일함에 폴더에 접근 한다.
            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            //4) 받은 메세지 정보를 순차별로 담고, 반환한다.

            List<Message> messages = Arrays.asList(emailFolder.getMessages());
            List<EmailDTO> emailDTOList = new ArrayList<>();

            for (int i = 0; i < messages.size(); i++) {
                EmailDTO emailDTO = new EmailDTO();

                emailDTO.setFromAddress(messages.get(i).getFrom()[0]);
                emailDTO.setContent(messages.get(i).getContent());
                emailDTO.setSubject(messages.get(i).getSubject());
                //Date 타입을 LocalDate타입으로 변환(타임리프에서 편하게 사용하기 위함)
                LocalDateTime localDateTime = new Timestamp(messages.get(i).getSentDate().getTime()).toLocalDateTime();
                emailDTO.setSentDate(localDateTime);
                emailDTO.setMessageNumber(messages.get(i).getMessageNumber());
                emailDTOList.add(emailDTO);

            }

            //5) 사용한 자원을 반환.
            emailFolder.close(false);
            emailStore.close();
            return emailDTOList;

        } catch (NoSuchProviderException e) {e.printStackTrace();}
        catch (MessagingException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return null;
    }

    @Override
    public EmailDTO viewMail(int messageNumber, List<EmailDTO> emailDTOList) {

        EmailDTO email = emailDTOList.get(messageNumber);

        return email;
    }
}
