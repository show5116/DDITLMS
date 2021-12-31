package com.example.dditlms.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LectureSection {
    CULTURE_SELECT("교선"),
    CULTURE_REQUIRED("교필"),
    MAJOR_SELECT("전선"),
    MAJOR_REQUIRED("전필");

    private static final Map<String,String> CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(LectureSection::getKorean,LectureSection::name))
    );
    public static LectureSection of(final String korean){
        return LectureSection.valueOf(CODE_MAP.get(korean));
    }

    private String korean;
}
