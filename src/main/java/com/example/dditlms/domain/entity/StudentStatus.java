package com.example.dditlms.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StudentStatus {
    ATTEND("재학"),
    ABSENCE("휴학"),
    GRADUATE("졸업");

    final private String krName;

    StudentStatus(String krName) {
        this.krName = krName;
    }

    public String getKrName(){
        return krName;
    }
}
