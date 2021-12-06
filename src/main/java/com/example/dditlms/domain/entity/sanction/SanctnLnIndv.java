package com.example.dditlms.domain.entity.sanction;

import com.example.dditlms.domain.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "SANCTN_LN_INDV")
@Entity
@RequiredArgsConstructor
@ToString
public class SanctnLnIndv {
    @Id
    @Column(name = "SANCTN_LN_SN", nullable = false)
    private Long sanctnLnIndvId;

    @Column(name = "SANCTNER", length = 200)
    private String sanctner;

    @Column(name = "SANCTN_SN")
    private Long sanctnSn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Employee mberNo;

    public Employee getMberNo() {
        return mberNo;
    }

    public void setMberNo(Employee mberNo) {
        this.mberNo = mberNo;
    }

    public Long getSanctnSn() {
        return sanctnSn;
    }

    public void setSanctnSn(Long sanctnSn) {
        this.sanctnSn = sanctnSn;
    }

    public String getSanctner() {
        return sanctner;
    }

    public void setSanctner(String sanctner) {
        this.sanctner = sanctner;
    }

    public Long getSanctnLnIndvId() {
        return sanctnLnIndvId;
    }

    public void setSanctnLnIndvId(Long id) {
        this.sanctnLnIndvId = id;
    }
}