package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrolmentDTO {
    private Student student;

    private Integer pnt;

    private OpenLecture openLecture;

    private Grade grade;

    private Major major;

    private String avgScore;
}
