package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.dto.PreCourseDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PRE_LEC")
@SequenceGenerator(
        name="PRE_LEC_SEQ_GEN",
        sequenceName = "PRE_LEC_SEQ",
        initialValue =1
)
public class PreCourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PRE_LEC_SEQ_GEN")
    @Column(name="PRE_SN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Student studentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESTBL_COURS_CD")
    private OpenLecture lectureCode;

    public PreCourseDTO toPreDto(){
        PreCourseDTO dto = PreCourseDTO.builder()
                .lectureCode(this.lectureCode.getId())
                .lectureSeme(this.lectureCode.getLectureSection().getKorean())
                .lectureName(this.lectureCode.getSubjectCode().getName())
                .professor(this.lectureCode.getProfessorNo()+"")
                .lectureSchedule(this.lectureCode.getLectureSchedule())
                .lectureRoom(this.lectureCode.getLectureId().getId())
                .subjectCode(this.lectureCode.getSubjectCode().getId()).build();
        return dto;
    }

}