package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Major_Selection {
    HUMANITIES("인문계열"),
    SOCIAL("사회계열"),
    EDUCATION("교육계열"),
    ENGINEERING("공학계열"),
    NATURE("자연계열"),
    ARTSANDPHYSICAL("예체능계열");

    private String name;

    }
