package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.CurriculumDTO;
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
@SequenceGenerator(
        name="CRCLM_SEQ_GEN",
        sequenceName = "CRCLM_SEQ",
        initialValue =1
)
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "CRCLM_SEQ_GEN")
    @Column(name = "CRCLM_CODE")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="LCTRE_SE")
    private LectureSection lectureSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CRCLM_SEME")
    private SemesterByYear semester;

    @Enumerated(EnumType.STRING)
    @Column(name="CRCLM_GRADE")
    private Grade grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MAJOR_CODE")
    private Major major;

    public CurriculumDTO toDTO(){
        CurriculumDTO curriculumDTO = CurriculumDTO.builder()
                .majorId(this.major.getId())
                .majorName(this.major.getKorean())
                .lectureSection(this.lectureSection)
                .sectionName(this.lectureSection.getKorean())
                .grade(this.grade)
                .gradeName(this.grade.getKorean())
                .subjectId(this.subject.getId())
                .subjectName(this.subject.getName())
                .point(this.subject.getPoint())
                .semesterId(this.semester.getYear()).build();
        return curriculumDTO;
    }
}
