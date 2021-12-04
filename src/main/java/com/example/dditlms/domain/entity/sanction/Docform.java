package com.example.dditlms.domain.entity.sanction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "DOCFORM")
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Docform {
    @Id
    @Column(name = "DOCFORM_CODE", nullable = false, length = 50)
    private String docFormId;

    @Column(name = "DOCFORM_NM")
    private String docformNm;

    @Lob
    @Column(name = "DOCFORM_CN")
    private String docformCn;

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

    public String getDocFormId() {
        return docFormId;
    }

    public void setDocFormId(String docFormId) {
        this.docFormId = docFormId;
    }

    @Builder
    public Docform(String docFormId, String docformNm, String docformCn) {
        this.docFormId = docFormId;
        this.docformNm = docformNm;
        this.docformCn = docformCn;
    }
}