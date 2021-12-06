package com.example.dditlms.domain.common;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_PROFESSOR("ROLE_PROFESSOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;

    Role(String value){
        this.value = value;
    }
}
