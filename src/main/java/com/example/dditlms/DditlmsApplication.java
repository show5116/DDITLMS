package com.example.dditlms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
public class DditlmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DditlmsApplication.class, args);
    }

}
