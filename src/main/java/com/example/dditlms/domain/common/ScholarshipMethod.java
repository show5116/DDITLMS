package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScholarshipMethod {
    PAYMENTS("지급"),
    REDUCTION("감면");

    private String korean;
}
