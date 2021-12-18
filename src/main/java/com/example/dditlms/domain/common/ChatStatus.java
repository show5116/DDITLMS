package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatStatus  {
    NOTREAD("not read","안 읽음"),
    READ("read","읽음"),
    DELETED("deleted","삭제됨");

    private String value;
    private String korean;
}
