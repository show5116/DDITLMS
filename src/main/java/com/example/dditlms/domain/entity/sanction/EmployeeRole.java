package com.example.dditlms.domain.entity.sanction;

public enum EmployeeRole {
    STAFF("사원"),
    ASSISTANT_MANAGER("대리"),
    MANAGER("과장"),
    GENERAL_MANAGER("부장")
    ;

    final private String krName;

    EmployeeRole(String krName) {this.krName = krName;}

    public String getKrName() {return krName;}
}
