package com.example.dditlms.domain.entity.sanction;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SANCTN_LN")
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SanctnLn {
    @Id
    @Column(name = "SANCTN_LN_ID", nullable = false)
    private Long sanctnLnId;

    @Column(name = "SANCTNER", length = 50)
    private String sanctner;

    @Column(name = "SANCTN_SN")
    private Long sanctnSn;

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

    public Long getSanctnLnId() {
        return sanctnLnId;
    }

    public void setSanctnLnId(Long sanctnLnId) {
        this.sanctnLnId = sanctnLnId;
    }
}