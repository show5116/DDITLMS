package com.example.dditlms.domain.entity.sanction;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SANCTN")
@Entity
@RequiredArgsConstructor
@ToString
public class Sanctn {
    @Id
    @Column(name = "SANCTN_SN", nullable = false)
    private Long sanctnId;

    @Column(name = "SANCTN_SJ")
    private String sanctnSj;

    @Lob
    @Column(name = "SANCTN_CN")
    private String sanctnCn;

    @Column(name = "SANCTN_WRITNGDE")
    private LocalDate sanctnWritngde;

    @Column(name = "SANCTN_UPDDE")
    private LocalDate sanctnUpdde;

    @Column(name = "DRAFTER")
    private Long drafter;

    @Column(name = "SANCTN_PROGRS_STTUS", length = 30)
    private String sanctnProgrsSttus;

    @Column(name = "ATCHMNFL_ID")
    private Long atchmnflId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCFORM_SN")
    private Docform docformSn;

    public Docform getDocformSn() {
        return docformSn;
    }

    public void setDocformSn(Docform docformSn) {
        this.docformSn = docformSn;
    }

    public Long getAtchmnflId() {
        return atchmnflId;
    }

    public void setAtchmnflId(Long atchmnflId) {
        this.atchmnflId = atchmnflId;
    }

    public String getSanctnProgrsSttus() {
        return sanctnProgrsSttus;
    }

    public void setSanctnProgrsSttus(String sanctnProgrsSttus) {
        this.sanctnProgrsSttus = sanctnProgrsSttus;
    }

    public Long getDrafter() {
        return drafter;
    }

    public void setDrafter(Long drafter) {
        this.drafter = drafter;
    }

    public LocalDate getSanctnUpdde() {
        return sanctnUpdde;
    }

    public void setSanctnUpdde(LocalDate sanctnUpdde) {
        this.sanctnUpdde = sanctnUpdde;
    }

    public LocalDate getSanctnWritngde() {
        return sanctnWritngde;
    }

    public void setSanctnWritngde(LocalDate sanctnWritngde) {
        this.sanctnWritngde = sanctnWritngde;
    }

    public String getSanctnCn() {
        return sanctnCn;
    }

    public void setSanctnCn(String sanctnCn) {
        this.sanctnCn = sanctnCn;
    }

    public String getSanctnSj() {
        return sanctnSj;
    }

    public void setSanctnSj(String sanctnSj) {
        this.sanctnSj = sanctnSj;
    }

    public Long getSanctnId() {
        return sanctnId;
    }

    public void setSanctnId(Long sanctnId) {
        this.sanctnId = sanctnId;
    }
}