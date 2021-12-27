package com.example.dditlms.domain.entity.sanction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SANCTN")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name="SANCTN_SEQ_GEN",
        sequenceName = "SANCTN_SEQ",
        initialValue =1
)
public class Sanctn {
    @Id
    @Column(name = "SANCTN_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "SANCTN_SEQ_GEN")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "SANCTN_PROGRS_STTUS", length = 30)
    private SanctnProgress status ;

    @Column(name = "ATCHMNFL_ID")
    private Long atchmnflId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCFORM_SN")
    private Docform docform;

}