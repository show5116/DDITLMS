package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ResultStatus {
    STANDBY("대기"),
    COMPANION("반려"),
    APPROVAL("승인");

    private String korean;
}
