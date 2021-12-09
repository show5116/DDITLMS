package com.example.dditlms.domain.entity.sanction;


public enum DocFormCategory {
    COMMON("공용문서"),
    ACADEMIC("교무처"),
    ADMINISTRATION("행정과"),
    GENERAL_AFFAIRS("총무부")
    ;

    final private String krName;

    DocFormCategory(String krName) {
        this.krName = krName;
    }

    public String getKrName() { return krName; }
}
