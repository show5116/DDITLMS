package com.example.dditlms.domain.entity.sanction;

import com.example.dditlms.domain.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SANCTN_LN")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class SanctnLn {
    @Id
    @Column(name = "SANCTN_LN_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
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


}