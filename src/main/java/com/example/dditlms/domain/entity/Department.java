package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Table(name = "DEPT")
@Entity
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Department {
    @Id
    @Column(name = "DEPT_CODE", nullable = false)
    private Long departmentCode;

    @Column(name = "DEPT_SE", length = 200)
    private String deptSe;

    @Column(name = "DEPT_NM", length = 200)
    private String deptNm;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_DEPT_CODE")
    private Department parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="PAR_DEPT_CODE")
    private List<Department> children;

}