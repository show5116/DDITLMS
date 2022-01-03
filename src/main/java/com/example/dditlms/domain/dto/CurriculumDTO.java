package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CurriculumDTO {
    private String semesterId;

    private String majorId;

    private String majorName;

    private String subjectId;

    private String subjectName;

    private Integer point;

    private Grade grade;

    private String gradeName;

    private LectureSection lectureSection;

    private String sectionName;
}
