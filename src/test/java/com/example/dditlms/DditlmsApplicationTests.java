package com.example.dditlms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.dditlms.mail.ReceiveMail.receiveEmail;


@SpringBootTest
class DditlmsApplicationTests {


    @Test
    void mailReceive(){

        String host = "mail.ddit.site";//change accordingly
        String mailStoreType = "pop3";
        String username= "test2@ddit.site";
        String password= "test123";//change accordingly

        receiveEmail(host, mailStoreType, username, password);


    }


}
