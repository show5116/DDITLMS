package com.example.dditlms.domain.common;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Menu {
    MYPAGE("개인정보 변경","/mypage","home"),
    SCRAP("스크랩","/scrap","home"),
    ADDRESS("주소록","/address","home"),
    CLOUD("클라우드","/cloud","git-pull-request"),
    CALENDAR("캘린더","/calendar","calendar"),
    MAIL("메일","/mail","mail"),
    GENERALNOTICE("일반 공지","/community/general","book-open"),
    SCHOLARSHIPNOTICE("장학 공지","/community/scholarship","book-open"),
    BACHELORNOTICE("학사 공지","/community/bachelor","book-open"),
    MAJORNOTICE("학과 공지","/community/major","book-open"),
    FREEBOARD("자유 게시판","/community/freeboard","book-open");

    private static final Map<String,String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Menu::getUrl,Menu::name))
    );

    private String name;
    private String url;
    private String icon;

    Menu(String name,String url ,String icon){
        this.name = name;
        this.url = url;
        this.icon = icon;
    }

    public static Menu of(final String url){
        return Menu.valueOf(CODE_MAP.get(url));
    }
}
