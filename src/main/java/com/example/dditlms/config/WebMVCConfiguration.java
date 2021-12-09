package com.example.dditlms.config;

import com.example.dditlms.util.AmazonS3Util;
import com.example.dditlms.util.OtpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class WebMVCConfiguration {

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSize(50 * 1024 * 1024);
        return commonsMultipartResolver;
    }

    @Bean
    public OtpUtil createOtpUtil(){
        return new OtpUtil();
    }
    @Bean
    public AmazonS3Util createAmazonS3Util() { return  new AmazonS3Util(); }

}
