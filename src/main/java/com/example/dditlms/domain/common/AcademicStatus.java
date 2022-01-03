package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AcademicStatus {
    ATTENDING("ATTENDING","재학"),
    TAKEABREAK("TAKEABREAK","휴학"),
    COMPLETION("COMPLETION","수료"),
    GRADUATE("GRADUATE","졸업"),
    EXPULSION("EXPULSION","제적"),
    WITHDRAW("WITHDRAW","퇴학"),
    CHANGEMAJOR("CHANGEMAJOR", "전과");

    private String value;
    private String korean;

}
