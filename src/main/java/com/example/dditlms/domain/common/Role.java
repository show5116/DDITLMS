package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_PROFESSOR("ROLE_PROFESSOR"),
    ROLE_ACCADEMIC_DEP("ROLE_ACCADEMIC_DEP"),
    ROLE_ADMIN_DEP("ROLE_ADMIN_DEP"),
    ROLE_GENERAL_DEP("ROLE_GENERAL_DEP"),
    ROLE_STUDENT_DEP("ROLE_STUDENT_DEP");

    private String value;

}
