package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.common.WheatherToDelete;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubjectDTO {

    private String id;

    private String name;

    private String completionDiv;

    private String courseOutline;

    private Integer point;

    private String parent;

    private WheatherToDelete status;

}
