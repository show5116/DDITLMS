package com.example.dditlms.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface PreCourseRegistrationService {
    @Transactional
    void preCourseRegistration(Map<String, Object> map);

    @Transactional
    void searchLecture(Map<String, Object> map);

    @Transactional
    void searchSubject(Map<String, Object> map);
}
