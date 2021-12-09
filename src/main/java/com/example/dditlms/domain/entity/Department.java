package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "DEPT")
@Entity
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Department {
    @Id
    @Column(name = "DEPT_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentCode;

    @Column(name = "DEPT_SE", length = 200)
    private String deptSe;

    @Column(name = "DEPT_NM", length = 200)
    private String deptNm;

    @Column(name = "PAR_DEPT_CODE", length = 200)
    private String parDeptCode;


}