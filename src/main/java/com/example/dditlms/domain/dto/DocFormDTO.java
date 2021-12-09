package com.example.dditlms.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class DocFormDTO {
    private String docform_nm;
    private Long docform_sn;

    public DocFormDTO() {}

    @QueryProjection
    public DocFormDTO(String docform_nm, Long docform_sn) {
        this.docform_nm = docform_nm;
        this.docform_sn = docform_sn;
    }
}
