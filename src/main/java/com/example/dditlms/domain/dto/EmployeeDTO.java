package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.entity.sanction.EmployeeRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EmployeeDTO {
    private EmployeeRole employeeRole;
    private String name;
    private String dept_nm;
    private Long userNumber;

    public EmployeeDTO() {}

    @QueryProjection
    public EmployeeDTO(EmployeeRole employeeRole, String name, Long userNumber) {
        this.employeeRole = employeeRole;
        this.name = name;
        this.userNumber = userNumber;
    }
    @QueryProjection
    public EmployeeDTO(EmployeeRole employeeRole, String dept_nm) {
        this.employeeRole = employeeRole;
        this.dept_nm = dept_nm;
    }
}
