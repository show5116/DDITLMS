package com.example.dditlms.security;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("ROLE_STUDENT"),
    PROFESSOR("ROLE_PROFESSOR"),
    ADMIN("ROLE_ADMIN");

    String value;

    Role(String value){
        this.value = value;
    }
}
