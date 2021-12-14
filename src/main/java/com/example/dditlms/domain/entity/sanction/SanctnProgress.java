package com.example.dditlms.domain.entity.sanction;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SanctnProgress {
    PROGRESS("진행"),
    COMPLETION("완료"),
    REJECT("반려"),
    PUBLICIZE("공람")
    ;

    final private String krName;

    SanctnProgress(String krName) {this.krName = krName;}

    public String getKrName() {return krName;}
}
