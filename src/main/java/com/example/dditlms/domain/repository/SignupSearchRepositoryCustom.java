package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.OpenLecture;

import java.util.List;
import java.util.Map;

public interface SignupSearchRepositoryCustom {
    List<OpenLecture> searchSubject(String searchSubject);

    //개설강의 전체 리스트
    public List<SignupDTO> totalLectureList(Map<String,Object> searchSubject);
}
