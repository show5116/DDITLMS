package com.example.dditlms.domain.entity.sanction;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "DOCFORM")
@Entity
@RequiredArgsConstructor
@ToString
public class Docform {
    @Id
    @Column(name = "DOCFORM_SN", nullable = false)
    private Long docformId;

    @Column(name = "DOCFORM_NM")
    private String docformNm;

    @Lob
    @Column(name = "DOCFORM_CN")
    private String docformCn;

    @Column(name = "DOCFORM_CN_CTGRY", length = 50)
    private String docformCnCtgry;

    public String getDocformCnCtgry() {
        return docformCnCtgry;
    }

    public void setDocformCnCtgry(String docformCnCtgry) {
        this.docformCnCtgry = docformCnCtgry;
    }

    public String getDocformCn() {
        return docformCn;
    }

    public void setDocformCn(String docformCn) {
        this.docformCn = docformCn;
    }

    public String getDocformNm() {
        return docformNm;
    }

    public void setDocformNm(String docformNm) {
        this.docformNm = docformNm;
    }

    public Long getDocformId() {
        return docformId;
    }

    public void setDocformId(Long docformId) {
        this.docformId = docformId;
    }
}