package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WheatherToDelete {
    DELETED("삭제"),
    EXISTED("존재");

    private String korean;
}
