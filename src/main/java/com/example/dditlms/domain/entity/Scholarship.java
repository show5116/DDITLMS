package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.common.ScholarshipStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="EFLN")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Getter @Setter
@SequenceGenerator(
        name="EFLN_SEQ_GEN",
        sequenceName = "EFLN_SEQ",
        initialValue =1
)
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "EFLN_SEQ_GEN")
    @Column(name="EFLN_SN")
    private Long id;

    @Column(name="SCHLSHIP_AM")
    private Long price;

    @Column(name="PYMNT_MTHD")
    @Enumerated(EnumType.STRING)
    private ScholarshipMethod method;

    @Column(name="DCSN_DE")
    private Date apliDate;

    @Column(name="PYMNT_DE")
    private Date completeDate;

    @ManyToOne
    @JoinColumn(name="EFLN_KND")
    private ScholarshipKind kind;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="REQST_SEMSTR")
    private SemesterByYear semester;

    @ManyToOne
    @JoinColumn(name="MBER_NO")
    private Student student;

    @Column(name="ATCHMNFL_ID")
    private Long attachment;

    @Column(name="EFLN_RM")
    @Enumerated(EnumType.STRING)
    private ScholarshipStatus status;

}
