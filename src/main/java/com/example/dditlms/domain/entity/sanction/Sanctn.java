package com.example.dditlms.domain.entity.sanction;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SANCTN")
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Sanctn {
    @Id
    @Column(name = "SANCTN_ID", nullable = false)
    private Long sanctnId;

    @Column(name = "SANCTN_STTUS", length = 30)
    private String sanctnSttus;

    @Column(name = "SANCTN_DATE")
    private LocalDate sanctnDate;

    @Lob
    @Column(name = "SANCTN_OPINION")
    private String sanctnOpinion;

    @Column(name = "SANCTN_STEP")
    private Long sanctnStep;

    @Column(name = "MBER_NO")
    private Long mberNo;

    @Column(name = "SANCTN_DOC_ID")
    private Long sanctnDocId;

    @Column(name = "SANCTN_LN_ID")
    private Long sanctnLnId;

    public Long getSanctnLnId() {
        return sanctnLnId;
    }

    public void setSanctnLnId(Long sanctnLnId) {
        this.sanctnLnId = sanctnLnId;
    }

    public Long getSanctnDocId() {
        return sanctnDocId;
    }

    public void setSanctnDocId(Long sanctnDocId) {
        this.sanctnDocId = sanctnDocId;
    }

    public Long getMberNo() {
        return mberNo;
    }

    public void setMberNo(Long mberNo) {
        this.mberNo = mberNo;
    }

    public Long getSanctnStep() {
        return sanctnStep;
    }

    public void setSanctnStep(Long sanctnStep) {
        this.sanctnStep = sanctnStep;
    }

    public String getSanctnOpinion() {
        return sanctnOpinion;
    }

    public void setSanctnOpinion(String sanctnOpinion) {
        this.sanctnOpinion = sanctnOpinion;
    }

    public LocalDate getSanctnDate() {
        return sanctnDate;
    }

    public void setSanctnDate(LocalDate sanctnDate) {
        this.sanctnDate = sanctnDate;
    }

    public String getSanctnSttus() {
        return sanctnSttus;
    }

    public void setSanctnSttus(String sanctnSttus) {
        this.sanctnSttus = sanctnSttus;
    }

    public Long getSanctnId() {
        return sanctnId;
    }

    public void setSanctnId(Long sanctnId) {
        this.sanctnId = sanctnId;
    }
}