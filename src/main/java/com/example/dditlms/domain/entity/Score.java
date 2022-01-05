package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.dto.EnrolmentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SCORE")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@SequenceGenerator(
        name="SCORE_SEQ_GEN",
        sequenceName = "SCORE_SEQ",
        initialValue =1
)
public class Score {

    @Id
    @Column(name = "SCRE_SN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "SCORE_SEQ_GEN")
    private Long scoreIdx;

    @Column(name = "ATEND_SCORE")
    private Integer attendance;

    @Column(name = "TASKS_CORE")
    private Integer taskScore;

    @Column(name = "MIDDLE_EXPR_SCORE")
    private Integer middleScore;

    @Column(name = "TRMEND_EXPR_SCORE")
    private Integer finalScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
      @JoinColumn(name = "MBER_NO",
            referencedColumnName = "MBER_NO"),
      @JoinColumn(name = "ESTBL_COURS_CD",
            referencedColumnName = "ESTBL_COURS_CD")
    })
    private Enrolment enrolment;

}
