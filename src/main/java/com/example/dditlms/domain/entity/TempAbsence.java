package com.example.dditlms.domain.entity;


import com.example.dditlms.domain.common.ResultStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TMPABSSKL")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@SequenceGenerator(
        name="TMPABSSKL_SEQ_GEN",
        sequenceName = "TMPABSSKL_SEQ",
        initialValue =1
)
public class TempAbsence {

    @Id
    @Column(name = "TMPABSSKL_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "TMPABSSKL_SEQ_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Student mberNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BEGINSEMSTR")
    private SemesterByYear beginsemstr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENDSEMSTR")
    private SemesterByYear endsemstr;

    @Enumerated(EnumType.STRING)
    @Column(name ="TMPABSSKL_ST")
    private ResultStatus status;
}
