package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.idclass.EnrolmentId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ATNLC_LCTRE")
@IdClass(EnrolmentId.class)
public class Enrolment {

    @Id
    @ManyToOne
    @JoinColumn(name = "MBER_NO")
    private Student student;

    @Column(name = "PNT")
    private Double pnt;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTBL_COURS_CD")
    private OpenLecture openLecture;

    @Enumerated(EnumType.STRING)
    @Column(name="HIST_GRADE")
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="HIST_MAJOR_CODE")
    private Major major;

}