package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.*;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.SemesterByYearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CourseRegistController {

    private final PreCourseRegistrationRepository preCourseRegistrationRepository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;
    private final StudentRepository studentRepository;
    private final EnrolmentRepository repository;

    private String memNo;
    private String memName;
    private String memRole;
    private Student student;
    private Grade studentGrade;

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

        Long studentNo = Long.parseLong(memNo);
        student = studentRepository.findById(studentNo).get();
        studentGrade = student.getGrade();
    }

    @GetMapping("/student/courseRegistration")
    public String courseRegistration(Model model) {
        getUserInfo();
        List<Major> majorList = majorRepository.findAll();

        SemesterByYear semester = semesterByYearRepository.selectNextSeme().get();
        List<OpenLecture> openLectureList = searchRepository.findAllByYearSeme(semester);
        List<PreCourseDTO> preCourseDTOList = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
            String id = openLecture.getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = openLecture.getLectureKind() +"";
            PreCourseDTO  dto = openLecture.toDto();
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(openLecture.getLectureSection().getKorean());
            if (kind.equals("F")){
                kind = "오프라인";
                dto.setLecturedivision(kind);
            } else if(kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
            }
            preCourseDTOList.add(dto);
        }
        int lectureCount = preCourseDTOList.size();

        List<PreCourseRegistration> getPreRegistrationList = preCourseRegistrationRepository.findByStudentNo(student);
        List<PreCourseDTO> preRegistrationList = new ArrayList<>();
        for (PreCourseRegistration preCourseRegistration : getPreRegistrationList){
            PreCourseDTO dto = PreCourseDTO.builder()
                    .lectureCode(preCourseRegistration.getLectureCode().getId())
                    .lectureSeme(preCourseRegistration.getLectureCode().getLectureSection().getKorean())
                    .lectureName(preCourseRegistration.getLectureCode().getSubjectCode().getName())
                    .professor(preCourseRegistration.getStudentNo().getMember().getName())
                    .lectureSchedule(preCourseRegistration.getLectureCode().getLectureSchedule())
                    .lectureRoom(preCourseRegistration.getLectureCode().getLectureId().getId())
                    .subjectCode(preCourseRegistration.getLectureCode().getSubjectCode().getId())
                    .build();
            preRegistrationList.add(dto);
        }
        int preCount = preRegistrationList.size();

        List<Enrolment> courseRegistrationList = repository.findAllByStudentAndGrade(student,studentGrade);
        List<PreCourseDTO> registRectureList = new ArrayList<>();
        for(Enrolment enrolment : courseRegistrationList){
            String id = enrolment.getOpenLecture().getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = enrolment.getOpenLecture().getLectureKind()+"";
            if(kind.equals("F")){
                kind = "오프라인";
            } else if(kind.equals("O")){
                kind = "온라인";
            }

            int applicantsCount = repository.countEnrolmentByOpenLecture(enrolment.getOpenLecture());

            String collegeName = enrolment.getOpenLecture().getMajorCode().getSelection().getName().split("대학")[0];
            PreCourseDTO dto = PreCourseDTO.builder()
                    .lectureCode(enrolment.getOpenLecture().getId())
                    .majorKr(enrolment.getOpenLecture().getMajorCode().getKorean())
                    .subjectCode(enrolment.getOpenLecture().getSubjectCode().getId())
                    .lectureName(enrolment.getOpenLecture().getSubjectCode().getName())
                    .lectureSeme(enrolment.getOpenLecture().getLectureSection().getKorean())
                    .maxPeopleCount(enrolment.getOpenLecture().getPeopleNumber())
                    .professor(enrolment.getOpenLecture().getProfessorNo().getMember().getName())
                    .lectureSchedule(enrolment.getOpenLecture().getLectureSchedule())
                    .lectureRoom(enrolment.getOpenLecture().getLectureId().getId())
                    .lectureClass(lectureClass)
                    .lecturedivision(kind)
                    .college(collegeName)
                    .applicantsCount(applicantsCount)
                    .build();
            registRectureList.add(dto);
        }
        int registCount = registRectureList.size();

        boolean contains = registRectureList.contains(preRegistrationList);
        log.info("-----CourseRegistController[courseRegistration] :: contains ={}", contains);

        model.addAttribute("registrationList", registRectureList);
        model.addAttribute("registCount", registCount);

        model.addAttribute("preRegistrationList", preRegistrationList);
        model.addAttribute("preTotalCount", preCount);

        String year = semester.getYear().split("-")[0];
        String seme = semester.getYear().split("-")[1];
        model.addAttribute("openLectureList", preCourseDTOList);
        model.addAttribute("openTotalCount", lectureCount);
        model.addAttribute("year",year);
        model.addAttribute("seme", seme);
        model.addAttribute("memberNo", memNo);
        model.addAttribute("memberName", memName);
        model.addAttribute("student", student);
        model.addAttribute("studentGrade", studentGrade);
        model.addAttribute("majorList", majorList);

        return "pages/courseRegistration";
    }



}
