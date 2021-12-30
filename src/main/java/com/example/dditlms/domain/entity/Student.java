package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.dto.StudentDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STDNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Student {

    @Id
    private Long userNumber;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MBER_NO")
    private Member member;

    @ManyToOne
    @JoinColumn(name="MAJOR_CODE")
    private Major major;

    @Column(name="ENTSCH_DE")
    private Date enterDate;

    @Enumerated(EnumType.STRING)
    @Column(name="GRADE")
    private Grade grade;

    @Column(name="STDNT_SEMSTR")
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name="STDNT_SE")
    private AcademicStatus academicStatus;

    public StudentDTO EntitytoDTO(){
        StudentDTO studentDTO = StudentDTO.builder()
                .userNumber(this.userNumber)
                .name(this.member.getName())
                .major(this.major.getKorean())
                .payment(this.major.getPayment()).build();
        return studentDTO;
    }
}
