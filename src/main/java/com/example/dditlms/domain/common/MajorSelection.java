package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MajorSelection {
    HUMANITIES("인문대학"),
    SOCIAL("사회과학대학"),
    EDUCATION("사범대학"),
    ENGINEERING("공과대학"),
    SCIENCE("자연과학대학"),
    ART("미술대학"),
    BUSINESS("경영대학");

    private String name;

    }
