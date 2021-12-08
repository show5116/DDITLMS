package com.example.dditlms.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EmployeeDTO {
    private String emp_se;
    private String dept_nm;

    public EmployeeDTO() {}

    @QueryProjection
    public EmployeeDTO(String emp_se, String dept_nm) {
        this.emp_se = emp_se;
        this.dept_nm = dept_nm;
    }
}
