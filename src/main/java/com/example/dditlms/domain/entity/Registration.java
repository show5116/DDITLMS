package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Grade;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="REGIST")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Getter @Setter
@SequenceGenerator(
        name="REGIST_SEQ_GEN",
        sequenceName = "REGIST_SEQ",
        initialValue = 1
)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "REGIST_SEQ")
    @Column(name="REGIST_SN")
    private Long id;

    @Column(name="REGIST_SEMSTR")
    private Date registDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SEMSTR")
    private SemesterByYear aplicationSemester;

    @Column(name="AMOUNT")
    private Long amount;

    @Column(name="FINAL_AMOUNT")
    private Long finalAmount;

    @Column(name="REGIST_GRADE")
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(name="STDNT_SEMSTR")
    private Integer semester;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MBER_NO")
    private Student student;
}
