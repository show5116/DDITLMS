package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.Grade;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STDNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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

    @Enumerated(EnumType.STRING)
    @Column(name="STDNT_SE")
    private AcademicStatus academicStatus;

}
