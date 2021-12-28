package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EFLN_KND")
@NoArgsConstructor
@Getter
public class ScholarshipKind {

    @Id
    @Column(name="EFLN_KND_CD")
    private String id;

    @Column(name="EFLN_KND_KR")
    private String korean;

    @Column(name="SCHLSHIP_AM")
    private Long price;

    @Column(name="EFLN_SEC")
    private String selectionCriteria;

}
