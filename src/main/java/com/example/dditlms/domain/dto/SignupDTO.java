package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.entity.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SignupDTO {
    private String yearSeme;
    private String majorKr;
    private String lectureSection;
    private String lectureCode;
    private String lectureName;
    private String professorName;
    private String lectureSchedule;
    private String lectureRoom;
    private int point;
    private int peopleNumber;
    private int fileId;


    public SignupDTO() {
    }

    @QueryProjection

    public SignupDTO(String yearSeme, String majorKr, String lectureSection, String lectureCode, String lectureName, String professorName, String lectureSchedule, String lectureRoom, int point, int peopleNumber, int fileId) {
        this.yearSeme = yearSeme;
        this.majorKr = majorKr;
        this.lectureSection = lectureSection;
        this.lectureCode = lectureCode;
        this.lectureName = lectureName;
        this.professorName = professorName;
        this.lectureSchedule = lectureSchedule;
        this.lectureRoom = lectureRoom;
        this.point = point;
        this.peopleNumber = peopleNumber;
        this.fileId = fileId;
    }
}