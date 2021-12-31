package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.WheatherToDelete;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
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
    private int point;               //과목학점

    @Enumerated(EnumType.STRING)
    @Column(name = "SBJECT_ST",nullable = false)
    @ColumnDefault(value = "'EXISTED'")
    private WheatherToDelete status;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    @JoinColumn(name = "PAR_SBJECT_CODE")
    private Subject parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_SBJECT_CODE")
    private List<Subject> children;    //선수과목 코드
}