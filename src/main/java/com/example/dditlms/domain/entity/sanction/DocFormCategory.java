package com.example.dditlms.domain.entity.sanction;


public enum DocFormCategory {
    COMMON("공용문서"),
    ACADEMIC("교무처"),
    ADMINISTRATION("행정지원과"),
    GENERAL_AFFAIRS("총무과"),
    STUDENT_DEP("학생과")
    ;

    final private String krName;

    DocFormCategory(String krName) {
        this.krName = krName;
    }

    public String getKrName() { return krName; }
}
