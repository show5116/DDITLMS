package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Grade;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Table(name="CRCLM")
@Getter @Setter
public class Curriculum {

    @Id
    @Column(name = "CRCLM_CODE")
    private String id;

    @Column(name="CRCLM_NM")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CRCLM_SEME")
    private SemesterByYear semester;

    @Enumerated(EnumType.STRING)
    @Column(name="CRCLM_GRADE")
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MAJOR_CODE")
    private Major major;
}
