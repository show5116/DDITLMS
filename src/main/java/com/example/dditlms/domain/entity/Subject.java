package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SBJECT")
public class Subject {

    @Id
    @Column(name = "SBJECT_CD", nullable = false, length = 200)
    private String id;

    @Column(name = "SBJECT_NM")
    private String name;

    @Column(name = "COMPL_SE", length = 50)
    private String completionDiv;     //이수구분

    @Column(name = "SBJECT_SUMRY")    //과목개요
    private String courseOutline;

    @Column(name = "SBJECT_PNT")
    private Long point;               //과목학점

    @Column(name = "DV_CLSSRM", length = 30)
    private String divisionClassroom;  //분반

    @Column(name = "PAR_SBJECT_CODE", length = 200)
    private String prerequisiteCode;    //선수과목 코드
}