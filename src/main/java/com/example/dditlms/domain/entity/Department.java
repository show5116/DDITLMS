package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DEPT")
@Entity
@ToString
@Getter
@Setter
@RequiredArgsConstructor
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


}