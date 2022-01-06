package com.example.dditlms.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PreCourseDTO {

    private String lectureCode;     // 강의코드
    private String majorKr;         // 전공
    private String subjectCode;     // 과목코드
    private String lectureName;     // 강의명
    private String lectureSeme;     // 이수구분(전필,전선..)
    private int maxPeopleCount;     // 정원
    private String professor;       // 교수명
    private String lectureSchedule; // 시간
    private String lectureRoom;     // 강의실
    private String lectureClass;    // 분반
    private String lecturedivision; // 강의구분(온,오프라인)
    private String college;         // 단대이름
    private int applicantsCount;    // 신청인원
    private String existence;       // 신청유무


    @QueryProjection
    public PreCourseDTO(String lectureCode) {
        this.lectureCode = lectureCode;
    }
}
