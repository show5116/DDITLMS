package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.*;
import com.example.dditlms.service.CourseRegistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRegistServiceImpl implements CourseRegistService {
    private final PreCourseRegistrationRepository preCourseRegistrationRepository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;
    private final EnrolmentRepository repository;

    private SemesterByYear semester;

    @Override
    @Transactional
    public void courseRegistration(Map<String, Object> map){
        Student student = (Student) map.get("student");

        semester = semesterByYearRepository.selectNextSeme().get();
        String year = semester.getYear().split("-")[0];
        String seme = semester.getYear().split("-")[1];

        /* 첫화면 개설강좌 리스트 조회 */
        List<OpenLecture> openLectureList = searchRepository.findAllByYearSeme(semester);
        List<PreCourseDTO> preCourseDTOList = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
            PreCourseDTO  dto = openLecture.toDto();
            int applicantsCount = repository.countEnrolmentByOpenLecture(openLecture);
            dto.setApplicantsCount(applicantsCount);
            preCourseDTOList.add(dto);
        }

        /* 첫화면 예비수강강좌 리스트 조회 */
        List<PreCourseRegistration> getPreRegistrationList = preCourseRegistrationRepository.findByStudentNo(student);
        for(PreCourseRegistration preCourseRegistration : getPreRegistrationList){
            String exist = preCourseRegistration.getExistence();

            log.info("-----CourseRegistService[courseRegistration] :: exist={}", exist);
        }

        /* 첫화면 수강신청 리스트 조회 */
        List<Enrolment> courseRegistrationList = repository.myPregidentList(student,semester.getYear());
        List<PreCourseDTO> registrationList = new ArrayList<>();
        for(Enrolment enrolment : courseRegistrationList){
            int applicantsCount = repository.countEnrolmentByOpenLecture(enrolment.getOpenLecture());
            PreCourseDTO dto = enrolment.getOpenLecture().toDto();
            dto.setApplicantsCount(applicantsCount);
            registrationList.add(dto);
        }

        map.put("year", year);
        map.put("seme", seme);
        map.put("openLectureList", preCourseDTOList);
        map.put("preRegistrationList", getPreRegistrationList);
        map.put("registration", registrationList);
    }

    @Override
    @Transactional
    public void searchLecture(Map<String, Object> map){
        String division = (String) map.get("division");
        LectureSection lectureDivision = null;
        if (!division.equals("total")){
            lectureDivision = LectureSection.valueOf(division);
        }
        String major = (String) map.get("majorName");
        List<OpenLecture> openLectureList = null;
        SemesterByYear semesterByYear = semesterByYearRepository.selectNextSeme().get();
        if (division.equals("total") && major.equals("total")){
            openLectureList = searchRepository.findAllByYearSeme(semesterByYear);
        } else if (division.equals("total")){
            Major majorName = majorRepository.findByKorean(major).get();
            openLectureList = searchRepository.findAllByYearSemeAndMajorCode(semesterByYear,majorName);
        } else if(major.equals("total")){
            openLectureList = searchRepository.findAllByYearSemeAndLectureSection(semesterByYear, lectureDivision);
        }else {
            Major majorName = majorRepository.findByKorean(major).get();
            openLectureList = searchRepository.findAllByYearSemeAndLectureSectionAndMajorCode(semesterByYear,lectureDivision,majorName);
        }

        List<PreCourseDTO> result = new ArrayList<>();
        for (OpenLecture openLecture : openLectureList){
            String id = openLecture.getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = openLecture.getLectureKind() +"";
            PreCourseDTO dto = openLecture.toDto();
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(openLecture.getLectureSection().getKorean());
            String a = dto.getLectureSeme();
            if (kind.equals("F")){
                kind = "오프라인";
                dto.setLecturedivision(kind);
            } else if (kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
            }
            result.add(dto);
        }
        int count = result.size();
        map.put("dto", result);
        map.put("totalCount", count);
    }

    @Override
    @Transactional
    public void searchSubject(Map<String, Object> map){
        String subject = (String) map.get("subject");
        List<OpenLecture> openLectureList = searchRepository.searchSubject(subject);
        List<PreCourseDTO> result = new ArrayList<>();
        for (OpenLecture openLecture : openLectureList){
            String id = openLecture.getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = openLecture.getLectureKind()+"";
            PreCourseDTO dto = openLecture.toDto();
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(openLecture.getLectureSection().getKorean());
            if (kind.equals("F")){
                kind = "오프라인";
                dto.setLecturedivision(kind);
            } else if (kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
            }
            result.add(dto);
        }
        int count = result.size();

        map.put("result", result);
        map.put("count", count);
    }

    @Override
    @Transactional
    public void preTotalRegistration(Map<String, Object> map){
        log.info("-----CourseRegistService[preTotalRegistration] ");

        List<String> preLectureList = (List<String>) map.get("preLectureList");
        Student student = (Student) map.get("student");

        List<String> successList = new ArrayList<>();
        for (String lectureCode : preLectureList){
            OpenLecture openLecture = searchRepository.findById(lectureCode).get();
            int applicantsCount = repository.countEnrolmentByOpenLecture(openLecture);  //신청인원수
            int maxCount = openLecture.getPeopleNumber();   // 해당강의 정원
            if (applicantsCount < maxCount){
                Enrolment enrolment = Enrolment.builder()
                        .student(student)
                        .openLecture(openLecture)
                        .grade(student.getGrade())
                        .major(student.getMajor())
                        .build();
                repository.save(enrolment);
                PreCourseRegistration preCourseRegistration = preCourseRegistrationRepository.findByStudentNoAndLectureCode(student,openLecture);
                preCourseRegistration.setExistence("1");
                successList.add(lectureCode);
            }
        }
        log.info("-----CourseRegistService[preTotalRegistration] :: successList={}", successList);

        List<Enrolment> courseRegistList = repository.myPregidentList(student,semester.getYear());
        List<PreCourseDTO> registrationList = new ArrayList<>();
        for (Enrolment enrolment : courseRegistList){
            int applicantsCount = repository.countEnrolmentByOpenLecture(enrolment.getOpenLecture());
            PreCourseDTO dto = enrolment.getOpenLecture().toDto();
            dto.setApplicantsCount(applicantsCount);
            registrationList.add(dto);
        }

        log.info("-----CourseRegistService[preTotalRegistration] :: registrationList={}", registrationList);

        map.put("successList",successList);
        map.put("registrationList",registrationList);
    }



}




























