package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.dto.SignupDTO;

import java.util.List;
import java.util.Map;

public interface SignupSearchRepositoryCustom {
    //개설강의 전체 리스트
    public List<SignupDTO> totalLectureList(Map<String,Object> searchSubject);
}
