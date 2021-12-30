package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum BoardCategory {
    FREEBOARD("자유게시판"),
    GENERAL("일반 공지"),
    BACHELORNOTICE("학사 공지"),
    MAJORNOTICE("학과 공지"),
    SCHOLARSHIPNOTICE("장학 공지");

    private String korean;

}
