package com.example.dditlms.domain.entity;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DEPT")
@Entity
@RequiredArgsConstructor
@ToString
public class Department {
    @Id
    @Column(name = "DEPT_CODE", nullable = false, length = 200)
    private String DepartmentCode;

    @Column(name = "DEPT_SE", length = 200)
    private String deptSe;

    @Column(name = "DEPT_NM", length = 200)
    private String deptNm;

    @Column(name = "PAR_DEPT_CODE", length = 200)
    private String parDeptCode;

    public String getParDeptCode() {
        return parDeptCode;
    }

    public void setParDeptCode(String parDeptCode) {
        this.parDeptCode = parDeptCode;
    }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }

    public String getDeptSe() {
        return deptSe;
    }

    public void setDeptSe(String deptSe) {
        this.deptSe = deptSe;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.DepartmentCode = departmentCode;
    }
}