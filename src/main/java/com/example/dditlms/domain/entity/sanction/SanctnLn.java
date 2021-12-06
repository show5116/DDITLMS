package com.example.dditlms.domain.entity.sanction;

import com.example.dditlms.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SANCTN_LN")
@Entity
@RequiredArgsConstructor
@ToString
public class SanctnLn {
    @Id
    @Column(name = "SANCTN_LN_SN", nullable = false)
    private Long sanctnLn;

    @Column(name = "SANCTN_STTUS", length = 30)
    private String sanctnSttus;

    @Column(name = "SANCTN_DATE")
    private LocalDate sanctnDate;

    @Lob
    @Column(name = "SANCTN_OPINION")
    private String sanctnOpinion;

    @Column(name = "SANCTN_STEP")
    private Long sanctnStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Member mberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SANCTN_SN")
    private Sanctn sanctnSn;

    public Sanctn getSanctnSn() {
        return sanctnSn;
    }

    public void setSanctnSn(Sanctn sanctnSn) {
        this.sanctnSn = sanctnSn;
    }

    public Member getMberNo() {
        return mberNo;
    }

    public void setMberNo(Member mberNo) {
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

    public Long getSanctnLn() {
        return sanctnLn;
    }

    public void setSanctnLn(Long sanctnLn) {
        this.sanctnLn = sanctnLn;
    }
}