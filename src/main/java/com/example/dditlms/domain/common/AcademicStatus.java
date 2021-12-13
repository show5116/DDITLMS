package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AcademicStatus {
    ATTENDING("ATTENDING","재학"),
    TAKEABREAK("TAKEABREAK","휴학"),
    COMPLETION("COMPLETION","수료"),
    GRADUATE("GRADUATE","졸업");

    private String value;
    private String korean;

}
