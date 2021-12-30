package com.example.dditlms.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreCourseDTO {

    private String lectureCode;
    private String majorKr;
    private String subjectCode;
    private String lectureName;
    private String lectureSeme;
    private int maxPeopleCount;
    private String professor;
    private String lectureSchedule;
    private String lectureRoom;
    private String lectureClass;
    private String lecturedivision;

    @QueryProjection
    public PreCourseDTO(String lectureCode) {
        this.lectureCode = lectureCode;
    }
}
