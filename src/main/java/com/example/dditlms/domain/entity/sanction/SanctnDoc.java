package com.example.dditlms.domain.entity.sanction;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "SANCTN_DOC")
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SanctnDoc {
    @Id
    @Column(name = "SANCTN_DOC_ID", nullable = false)
    private Long sanctnDocId;

    public Long getSanctnDocId() {
        return sanctnDocId;
    }

    public void setSanctnDocId(Long sanctnDocId) {
        this.sanctnDocId = sanctnDocId;
    }
}