package com.example.dditlms.domain.entity;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "EMP")
@Entity
@RequiredArgsConstructor
@ToString
public class Employee {
    @Id
    @Column(name = "MBER_NO", nullable = false)
    private Long empId;

    @Column(name = "EMP_SE", length = 50)
    private String empSe;

    @Column(name = "ENCPN", length = 200)
    private String encpn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPT_CODE")
    private Department departmentCode;

    public Department getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(Department departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getEncpn() {
        return encpn;
    }

    public void setEncpn(String encpn) {
        this.encpn = encpn;
    }

    public String getEmpSe() {
        return empSe;
    }

    public void setEmpSe(String empSe) {
        this.empSe = empSe;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }
}