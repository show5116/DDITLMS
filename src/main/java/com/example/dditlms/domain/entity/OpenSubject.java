package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ESTBL_COURS")
public class OpenSubject {
    @Id
    @Column(name = "ESTBL_COURS_CD", nullable = false, length = 200)
    private String id;

    @Column(name = "ATCHMNFL_ID")
    private Long syllabusFileId;        //첨부파일번호(강의계획서)

    @Column(name = "LCTRE_NMPR")
    private Long peopleNumber;

    @Column(name = "LCTRE_SE", length = 50)
    private String completionDiv;

    @Column(name = "LCTRE_KND")
    private Boolean lectureKind;

    @Column(name = "LCTRE_STUT", length = 200)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SBJECT_CD")
    private Subject subjectCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAJOR_CODE")
    private Major majorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "YEAR_SEME")
    private SemesterByYear openYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LCTRUM_CD")
    private LectureRoom lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Professor professorNo;

}