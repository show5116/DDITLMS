package com.example.dditlms.service;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.CurriculumDTO;
import com.example.dditlms.domain.entity.Curriculum;

import java.util.List;

public interface CurriculumService {
    List<Curriculum> searchCurriculums(String major, String semester, Grade grade, LectureSection section);
    List<Curriculum> deleteCurriculum(String major, String semester, Grade grade, LectureSection section,Long curriculumId);
    void excelCurriculum(List<CurriculumDTO> curriculumDTOList);
    List<CurriculumDTO> exportCurriculums();
    Boolean insertCurriculum(String major, String semester, Grade grade, LectureSection section,String subject);
}
