package com.example.dditlms;

import com.example.dditlms.config.WebMVCConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
@EnableScheduling
public class DditlmsApplication {

    private final static Logger LOG = LoggerFactory.getLogger(DditlmsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DditlmsApplication.class, args);
    }

}
