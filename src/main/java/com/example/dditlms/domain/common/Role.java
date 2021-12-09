package com.example.dditlms.domain.common;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_PROFESSOR("ROLE_PROFESSOR"),
    ROLE_ACCADEMIC_DEP("ROLE_ACCADEMIC_DEP"),
    ROLE_ADMIN_DEP("ROLE_ADMIN_DEP"),
    ROLE_GENERAL_DEP("ROLE_GENERAL_DEP"),
    ROLE_STUDENT_DEP("ROLE_STUDENT_DEP");

    private String value;

    Role(String value){
        this.value = value;
    }
}
