package com.example.dditlms.domain.entity.sanction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "DOCFORM")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Docform {
    @Id
    @Column(name = "DOCFORM_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long docformId;

    @Column(name = "DOCFORM_NM")
    private String docformNm;

    @Lob
    @Column(name = "DOCFORM_CN")
    private String docformCn;

    @Enumerated(EnumType.STRING)
    @Column(name = "DOCFORM_CN_CTGRY", length = 50)
    private DocFormCategory docFormCategory;


}