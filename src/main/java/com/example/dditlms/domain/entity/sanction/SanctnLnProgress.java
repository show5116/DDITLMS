package com.example.dditlms.domain.entity.sanction;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SanctnLnProgress {
    DRAFTER("기안자"),
    PROCESS("결재완료"),
    REQUEST("미결재"),
    WAITING("결재대기"),
    REJECT("반려처리")
    ;

    final private String krName;

    SanctnLnProgress(String krName) {this.krName = krName;}

    public String getKrName() {return krName;}
}
