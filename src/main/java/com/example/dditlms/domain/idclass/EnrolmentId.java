package com.example.dditlms.domain.idclass;

import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.Student;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class EnrolmentId implements Serializable {

    private Long student;

    private String openLecture;

}
