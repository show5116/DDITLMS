package com.example.dditlms.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface SignupSearchService {
    @Transactional
    void signUpSearch(Map<String, Object> map);

    @Transactional
    void getMajor(Map<String, Object> map);

    @Transactional
    void allAutoSearch(Map<String, Object> map);

    @Transactional
    void getLectureManageList(Map<String, Object> map);
}
