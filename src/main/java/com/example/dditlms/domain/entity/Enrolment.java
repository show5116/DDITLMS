package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.dto.EnrolmentDTO;
import com.example.dditlms.domain.idclass.EnrolmentId;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "ATNLC_LCTRE")
@IdClass(EnrolmentId.class)
public class Enrolment {

    @Id
    @ManyToOne
    @JoinColumn(name = "MBER_NO")
    private Student student;

    @Column(name = "PNT")
    private Integer pnt;

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

    public EnrolmentDTO toDTO(){
        EnrolmentDTO enrolmentDTO = EnrolmentDTO.builder()
                .student(this.student)
                .pnt(this.pnt)
                .openLecture(this.openLecture)
                .grade(this.grade)
                .major(this.major)
                .build();

        return enrolmentDTO;
    }

}