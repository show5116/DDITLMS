package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.PreCourseDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ESTBL_COURS")
public class OpenLecture {
    @Id
    @Column(name = "ESTBL_COURS_CD", nullable = false, length = 200)
    private String id;

    @Column(name = "LCTRE_SE", length = 50)
    @Enumerated(EnumType.STRING)
    private LectureSection lectureSection;

    @Column(name = "LCTRE_KND")
    private Character lectureKind;

    @Column(name = "LCTRE_NMPR")
    private int peopleNumber;

    @Column(name = "LCTRE_STUT", length = 200)
    private String state;

    @Column(name = "ATCHMNFL_ID")
    private int syllabusFileId;   //첨부파일번호(강의계획서)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LCTRUM_CD")
    private LectureRoom lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAJOR_CODE")
    private Major majorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "YEAR_SEME")
    private SemesterByYear yearSeme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Professor professorNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SBJECT_CD")
    private Subject subjectCode;

    @Column(name = "LCTRE_SCHEDULE", length = 30)
    private String lectureSchedule;

    public PreCourseDTO toDto(){
        String lectureClass = "";

        PreCourseDTO dto = PreCourseDTO.builder()
                .lectureCode(this.id)
                .majorKr(this.majorCode.getKorean())
                .subjectCode(this.subjectCode.getId())
                .lectureName(this.subjectCode.getName())
                .lectureSeme(this.lectureSection.getKorean())
                .maxPeopleCount(this.peopleNumber)
                .professor(this.professorNo.getMember().getName())
                .lectureSchedule(this.lectureSchedule)
                .lectureRoom(this.lectureId.getId())
                .lectureClass(lectureClass)
                .lecturedivision(this.lectureKind +"").build();
        return dto;
    }




}