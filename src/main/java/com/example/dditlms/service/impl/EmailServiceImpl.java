package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.EmailDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.EmailService;
import com.example.dditlms.util.base64Parser;
import com.sun.mail.pop3.POP3Store;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import lombok.extern.slf4j.Slf4j;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@Transactional(readOnly = true)
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public List<EmailDTO> receiveEmailList(String folderName) {

        //현재 로그인한 사용자 정보(userNumber)를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        Long userNumber = member.getUserNumber();
        String domain = "@ddit.site";

        String pop3Host = "mail.ddit.site";
        String storeType = "pop3";
        String user = (member.getMemberId() + domain);
        String password ="java";



        try {
            //1) 세션값 받아옴, 메일 수신은 pop3프로토콜로만 받기로 함.
            Properties properties = new Properties();
            properties.put("mail.pop3.host", pop3Host);
            Session emailSession = Session.getDefaultInstance(properties);

            //2) pop3서버로 연결 함.
            POP3Store emailStore = (POP3Store) emailSession.getStore(storeType);
            emailStore.connect(user, password);


            //3) 받은 메일함에 폴더에 접근 한다.
            Folder[] folders = emailStore.getDefaultFolder().list("*");
            log.info("-----------------"+ Arrays.toString(folders));
            Folder emailFolder = emailStore.getFolder(folderName);
            emailFolder.open(Folder.READ_ONLY);

            // 보낸메일, 휴지통 폴더를 생성한다.

//            Folder sentFolder = emailStore.getFolder("Sent");
//            Folder trashFolder = emailStore.getFolder("Trash");
//            if (!sentFolder.exists()) {
//                sentFolder.create(Folder.HOLDS_FOLDERS);
//                sentFolder.setSubscribed(true);
//                sentFolder.renameTo(sentFolder);
//            }

//            if (!trashFolder.exists()) {
//                trashFolder.create(Folder.HOLDS_MESSAGES);
//                trashFolder.renameTo(trashFolder);
//                trashFolder.setSubscribed(true);
//            }

            //4) 받은 메세지 정보를 순차별로 담고, 반환한다.


            List<Message> messages = Arrays.asList(emailFolder.getMessages());
            List<EmailDTO> emailDTOList = new ArrayList<>();


            for (int i = 0; i < messages.size(); i++) {
                EmailDTO emailDTO = new EmailDTO();

                String convertAddress = base64Parser.getFileName(messages.get(i).getFrom()[0].toString());
                InternetAddress internetAddress = new InternetAddress();
                internetAddress.setAddress(convertAddress);
                emailDTO.setFromAddress(internetAddress);

                emailDTO.setContent(messages.get(i).getContent());
                emailDTO.setSubject(messages.get(i).getSubject());
                Address[] toList = messages.get(i).getRecipients(MimeMessage.RecipientType.TO);
                emailDTO.setToList(toList);
                Address[] ccList = messages.get(i).getRecipients(MimeMessage.RecipientType.CC);
                emailDTO.setCcList(ccList);
                //Date 타입을 LocalDate타입으로 변환(타임리프에서 편하게 사용하기 위함)
                LocalDateTime localDateTime = new Timestamp(messages.get(i).getSentDate().getTime()).toLocalDateTime();
                emailDTO.setSentDate(localDateTime);
                emailDTO.setMessageNumber(messages.get(i).getMessageNumber());


                String contentType = messages.get(i).getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";
                String saveDirectory = "C:/temp/mail/";
                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) messages.get(i).getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";

                            part.saveFile(saveDirectory + File.separator + fileName);
                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }

                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = messages.get(i).getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
                emailDTO.setContent(messageContent);
                emailDTOList.add(emailDTO);
            }

            //5) 사용한 자원을 반환.
            emailFolder.close(false);
            emailStore.close();
            return emailDTOList;

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public EmailDTO viewMail(int messageNumber, List<EmailDTO> emailDTOList) {

        EmailDTO email = emailDTOList.get(messageNumber);

        return email;
    }

    @Override
    public void writeMail(EmailDTO emailDTO) {

        //현재 로그인한 사용자 정보(userNumber)를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {

        }

        String domain = "@ddit.site";
        String fromName = member.getName();
        String fromAddress = (member.getMemberId() + domain);

        String subject = emailDTO.getSubject();
        Object content = emailDTO.getContent();
        String toAddress = emailDTO.getToAddress();
        MultipartFile multipartFile = emailDTO.getMultipartFile();

        Email email = EmailBuilder.startingBlank()
                .from(fromName, fromAddress)
                .to(toAddress, toAddress)
                .withSubject(subject)
                .withHTMLText((String) content)
                .withAttachment(multipartFile.getName(), (DataSource) multipartFile.getResource())
                .buildEmail();

        Mailer inhouseMailer = MailerBuilder
                .withSMTPServer("mail.ddit.site", 25, fromAddress, "java")
                .buildMailer();

    }
}