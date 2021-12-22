package com.example.dditlms.domain.dto;

import lombok.Data;
import lombok.ToString;

import javax.mail.Address;
import java.time.LocalDateTime;

@Data
@ToString
public class EmailDTO {
    Address fromAddress;
    String toAddress;
    String subject;
    Object content;
    LocalDateTime sentDate;
    Integer messageNumber;
    String contentType;
    Address[] toList;
    Address[] ccList;
}
