package com.example.dditlms.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface CourseRegistService {
    @Transactional
    public void courseRegistration(Map<String, Object> map);

    @Transactional
    public void searchLecture(Map<String, Object> map);

    @Transactional
    public void searchSubject(Map<String, Object> map);

    @Transactional
    public void preTotalRegistration(Map<String, Object> map);

    @Transactional
    public void onePreRegistration(Map<String, Object> map);

    @Transactional
    public void courseRegistrationCancel(Map<String, Object> map);

    @Transactional
    public void confirmDupl(Map<String, Object> map);

    @Transactional
    public void oneOpenLectureRegist(Map<String, Object> map);
}
