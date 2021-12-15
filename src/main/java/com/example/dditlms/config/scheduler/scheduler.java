package com.example.dditlms.config.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class scheduler {

    //cron 규칙 (초 분 시 일 월 요일 연도(생략가능))
    //(0 0/1 * * * *) 매 1분마다 실행
    @Scheduled(cron = "0 0/1 * * * *")
    public void testCron(){
        System.out.println("테스트용");
    }
}
