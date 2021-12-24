package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Widget {
    TODOLIST("WID101","할일들"),
    WEATHER("WID102","날씨");

    private String code;
    private String korean;
}
