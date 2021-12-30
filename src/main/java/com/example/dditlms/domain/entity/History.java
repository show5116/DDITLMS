package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="HIST")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Getter
@Setter
@SequenceGenerator(
        name="HIST_SEQ_GEN",
        sequenceName = "HIST_SEQ",
        initialValue = 1
)
public class History {

    @Id
    @Column(name="HIST_SN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "HIST_SEQ_GEN")
    private Long id;

    @Column(name="HIST_CHANGE_REQSTMD")
    private Date aplicationDate;

    @Column(name="HIST_CHANGE_DE")
    private Date changeDate;

    @Enumerated(EnumType.STRING)
    @Column(name="HIST_STTUS")
    private AcademicStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name="HIST_RST")
    private ResultStatus resultStatus;

    @Column(name="HIST_NT")
    private String note;

    @ManyToOne
    @JoinColumn(name="MBER_NO")
    private Student student;
}
