package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRE_LEC")
@SequenceGenerator(
        name="PRE_LEC_SEQ_GEN",
        sequenceName = "PRE_LEC_SEQ",
        initialValue =1
)
public class PreCourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRE_LEC_SEQ_GEN")
    @Column(name="PRE_SN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Student studentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTBL_COURS_CD")
    private OpenLecture lectureCode;

}