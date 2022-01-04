package com.example.dditlms.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface CourseRegistService {
    @Transactional
    void courseRegistration(Map<String, Object> map);

    @Transactional
    void searchLecture(Map<String, Object> map);

    @Transactional
    void searchSubject(Map<String, Object> map);

    @Transactional
    void preTotalRegistration(Map<String, Object> map);

    @Transactional
    void onePreRegistration(Map<String, Object> map);

    @Transactional
    void courseRegistrationCancel(Map<String, Object> map);

    @Transactional
    void confirmDupl(Map<String, Object> map);

    @Transactional
    void oneOpenLectureRegist(Map<String, Object> map);
}
