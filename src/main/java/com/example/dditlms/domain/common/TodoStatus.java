package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TodoStatus {
    TODO("할일"),
    DONE("다한일");

    private String korean;
}
