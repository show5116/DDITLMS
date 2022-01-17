package com.example.dditlms.service.impl;


import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.*;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.PreCourseRegistrationService;
import com.example.dditlms.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreCourseRegistrationServiceImpl implements PreCourseRegistrationService {

    private final PreCourseRegistrationRepository repository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;
    private final AttachmentRepository attachmentRepository;

    private String memNo;
    private String memName;
    private String memRole;
    private SemesterByYear semester;
    private Student student;

    private void getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }
        memNo = member.getUserNumber()+"";
        memName = member.getName();
        memRole = member.getRole().getValue();

        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        semester = semesterWrapper.orElse(null);

        Long studentNo = Long.parseLong(memNo);
        student = Student.builder()
                .userNumber(studentNo).build();
    }

    @Override
    @Transactional
    public void preCourseRegistration(Map<String, Object> map){
        getUserInfo();
        String year = semester.getYear().split("-")[0];
        String seme = semester.getYear().split("-")[1];
        String yearSeme = semester.getYear();
        List<Major> majorList = majorRepository.findAll();

        /* 개설강의 리스트 */
        List<OpenLecture> openLectureList = searchRepository.findAllByYearSeme(semester);
        List<PreCourseDTO> preCourseDTOList = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
            int applicantsCount = repository.countAllByLectureCode(openLecture);
            PreCourseDTO  dto = openLecture.toDto();
            Attachment attachment = attachmentRepository.findByIdAndOrder(Long.valueOf(openLecture.getSyllabusFileId()),1).get();
            dto.setApplicantsCount(applicantsCount);
            dto.setFilePath(attachment.getOriginName());
            preCourseDTOList.add(dto);
        }

        /* 예비수강 리스트 */
        List<PreCourseRegistration> getPreRegistrationList = repository.findByStudentNo(student);
        List<PreCourseDTO> result = new ArrayList<>();

        for (PreCourseRegistration preCourseRegistration : getPreRegistrationList) {
            PreCourseDTO dto = PreCourseDTO.builder()
                    .lectureCode(preCourseRegistration.getLectureCode().getId())
                    .lectureSeme(preCourseRegistration.getLectureCode().getLectureSection().getKorean())
                    .lectureName(preCourseRegistration.getLectureCode().getSubjectCode().getName())
                    .professor(preCourseRegistration.getStudentNo().getMember().getName())
                    .lectureSchedule(preCourseRegistration.getLectureCode().getLectureSchedule())
                    .lectureRoom(preCourseRegistration.getLectureCode().getLectureId().getId())
                    .subjectCode(preCourseRegistration.getLectureCode().getSubjectCode().getId())
                    .build();
            result.add(dto);
        }
        map.put("semester",yearSeme);
        map.put("memNo", memNo);
        map.put("memName", memName);
        map.put("year", year);
        map.put("seme", seme);
        map.put("majorList", majorList);
        map.put("openLectureList", preCourseDTOList);
        map.put("preRegistrationList", result);
    }

    @Override
    @Transactional
    public void searchLecture(Map<String, Object> map){
        String getDivision = (String) map.get("division");
        String majorName = (String) map.get("majorName");

        List<OpenLecture> openLectureList = new ArrayList<>();

        if(getDivision.equals("total") && majorName.equals("total")){
            openLectureList = searchRepository.findAllByYearSeme(semester);
        }else if(getDivision.equals("total")){
            Optional<Major> majorWrapper = majorRepository.findByKorean(majorName);
            Major major = majorWrapper.orElse(null);
            openLectureList = searchRepository.findAllByYearSemeAndMajorCode(semester,major);
        }else if(majorName.equals("total")){
            LectureSection division = LectureSection.valueOf(getDivision);
            openLectureList = searchRepository.findAllByYearSemeAndLectureSection(semester,division);
        }else {
            Optional<Major> majorWrapper = majorRepository.findByKorean(majorName);
            Major major = majorWrapper.orElse(null);
            LectureSection division = LectureSection.valueOf(getDivision);
            openLectureList = searchRepository.findAllByYearSemeAndLectureSectionAndMajorCode(semester,division,major);
        }

        List<PreCourseDTO> result = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
            int applicantsCount = repository.countAllByLectureCode(openLecture);
            PreCourseDTO dto = openLecture.toDto();
            Attachment attachment = attachmentRepository.findByIdAndOrder(Long.valueOf(openLecture.getSyllabusFileId()),1).get();
            dto.setApplicantsCount(applicantsCount);
            dto.setFilePath(attachment.getOriginName());
            result.add(dto);
        }

        map.put("openLectureList", result);
    }

    @Override
    @Transactional
    public void searchSubject(Map<String, Object> map){
        String searchSubject = (String) map.get("searchSubject");

        List<OpenLecture> openLectureList = searchRepository.searchSubject(searchSubject);
        List<PreCourseDTO> result = new ArrayList<>();

        for (OpenLecture openLecture : openLectureList) {
            PreCourseDTO dto = openLecture.toDto();
            result.add(dto);
        }
        map.put("openLectureList", result);
    }











}
