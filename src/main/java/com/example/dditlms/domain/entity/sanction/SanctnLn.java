package com.example.dditlms.domain.entity.sanction;

import com.example.dditlms.domain.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "SANCTN_LN")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name="SANCTN_LN_SEQ_GEN",
        sequenceName = "SANCTN_LN_SEQ",
        initialValue =1
)
public class SanctnLn {
    @Id
    @Column(name = "SANCTN_LN_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "SANCTN_LN_SEQ_GEN")
    private Long sanctnLn;

    @Column(name = "SANCTN_STTUS", length = 30)
    @Enumerated(EnumType.STRING)
    private SanctnLnProgress sanctnLnProgress;

    @Column(name = "SANCTN_DATE")
    private LocalDateTime sanctnDate;

    @Column(name = "SANCTN_OPINION")
    private String sanctnOpinion;

    @Column(name = "SANCTN_STEP")
    private Integer sanctnStep;

    @Column(name = "SANCTN_LS_APV")
    private String lastApproval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Member mberNo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "SANCTN_SN")
    private Sanctn sanctnSn;


}