package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.CurriculumDTO;
import com.example.dditlms.domain.entity.Curriculum;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Subject;
import com.example.dditlms.domain.repository.CurriculumRepository;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SubjectRepository;
import com.example.dditlms.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;

    private final MajorRepository majorRepository;

    private final SemesterByYearRepository semesterRepository;

    private final SubjectRepository subjectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curriculum> searchCurriculums(String majorId, String semesterId, Grade grade, LectureSection section) {
        SemesterByYear semester = semesterRepository.findById(semesterId).get();
        Major major = majorRepository.findById(majorId).get();
        List<Curriculum> curriculumList = curriculumRepository.findAllBySemesterAndGradeAndMajorAndLectureSection(semester,grade,major,section);
        return curriculumList;
    }

    @Override
    @Transactional
    public List<Curriculum> deleteCurriculum(String majorId, String semesterId, Grade grade, LectureSection section, Long curriculumId) {
        SemesterByYear semester = semesterRepository.findById(semesterId).get();
        Major major = majorRepository.findById(majorId).get();
        Curriculum delete = curriculumRepository.findById(curriculumId).get();
        curriculumRepository.delete(delete);
        List<Curriculum> curriculumList = curriculumRepository.findAllBySemesterAndGradeAndMajorAndLectureSection(semester,grade,major,section);
        return curriculumList;
    }

    @Override
    public void excelCurriculum(List<CurriculumDTO> curriculumDTOList) {
        for(CurriculumDTO curriculumDTO : curriculumDTOList){
            try{
                SemesterByYear semester = semesterRepository.findById(curriculumDTO.getSemesterId()).get();
                Major major = majorRepository.findById(curriculumDTO.getMajorId()).get();
                Subject subject = subjectRepository.findById(curriculumDTO.getSubjectId()).get();
                Optional<Curriculum> duplicate = curriculumRepository.findBySemesterAndGradeAndMajorAndSubject(semester,curriculumDTO.getGrade(),major,subject);
                if(duplicate.orElse(null)==null){
                    Curriculum curriculum = Curriculum.builder()
                            .semester(semester)
                            .major(major)
                            .grade(curriculumDTO.getGrade())
                            .subject(subject)
                            .lectureSection(curriculumDTO.getLectureSection()).build();
                    curriculumRepository.save(curriculum);
                }
            }catch (Exception e){
            }
        }
    }

    @Override
    public List<CurriculumDTO> exportCurriculums() {
        List<CurriculumDTO> curriculumDTOList = new ArrayList<>();
        List<Curriculum> curriculumList = curriculumRepository.findAll(Sort.by("semester").and(Sort.by("major")).and(Sort.by("grade")).and(Sort.by("lectureSection")));
        for(Curriculum curriculum :curriculumList){
            curriculumDTOList.add(curriculum.toDTO());
        }
        return curriculumDTOList;
    }

    @Override
    @Transactional
    public Boolean insertCurriculum(String majorId, String semesterId, Grade grade, LectureSection section, String subjectId) {
        SemesterByYear semester = semesterRepository.findById(semesterId).get();
        Major major = majorRepository.findById(majorId).get();
        Subject subject = subjectRepository.findById(subjectId).get();
        Optional<Curriculum> duplicate = curriculumRepository.findBySemesterAndGradeAndMajorAndSubject(semester,grade,major,subject);
        if(duplicate.orElse(null)==null){
            Curriculum curriculum = Curriculum.builder()
                    .lectureSection(section)
                    .subject(subject)
                    .semester(semester)
                    .grade(grade)
                    .major(major).build();
            curriculumRepository.save(curriculum);
            return false;
        }
        return true;
    }
}
