package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.*;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.CourseRegistService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.dditlms.domain.entity.QMember.member;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CourseRegistController {
    private final CourseRegistService service;

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
        Member loginMember = MemberUtil.getLoginMember();
        memNo = loginMember.getUserNumber()+"";
        memName = loginMember.getName();
        memRole = loginMember.getRole().getValue();

        Long studentNo = Long.parseLong(memNo);
        student = studentRepository.findById(studentNo).get();
        studentGrade = student.getGrade();
    }

    @GetMapping("/student/courseRegistration")
    public String courseRegistration(Model model) {
        getUserInfo();
        Map<String, Object> map = new HashMap<>();
        map.put("student", student);
        service.courseRegistration(map);

        List<Major> majorList = majorRepository.findAll();
        List<PreCourseDTO> registrationList = (List<PreCourseDTO>) map.get("registration");
        List<PreCourseRegistration> preRegistrationList = (List<PreCourseRegistration>) map.get("preRegistrationList");
        List<PreCourseDTO> openLectureList = (List<PreCourseDTO>) map.get("openLectureList");
        String year = (String) map.get("year");
        String seme = (String) map.get("seme");

        model.addAttribute("registrationList", registrationList);
        model.addAttribute("preRegistrationList", preRegistrationList);
        model.addAttribute("openLectureList", openLectureList);

        model.addAttribute("year",year);
        model.addAttribute("seme", seme);
        model.addAttribute("memberNo", memNo);
        model.addAttribute("memberName", memName);
        model.addAttribute("student", student);
        model.addAttribute("studentGrade", studentGrade);
        model.addAttribute("majorList", majorList);

        return "pages/courseRegistration";
    }

    @PostMapping("/student/courseRegistration/searchLecture")
    public String searchLecture(Model model, @RequestParam Map<String, Object> paramMap){
        String division = (String) paramMap.get("division");
        String majorName = (String) paramMap.get("major");

        Map<String, Object> map = new HashMap<>();
        map.put("division", division);
        map.put("majorName", majorName);

        service.searchLecture(map);
        List<PreCourseDTO> openLectureList = (List<PreCourseDTO>) map.get("dto");
        int count = (int) map.get("totalCount");

        model.addAttribute("openLectureList", openLectureList);
        model.addAttribute("openTotalCount",count);

        return "pages/courseRegistration:: #open-lecture-tr";
    }

    @PostMapping("/student/courseRegistration/searchSubject")
    public String searchSubject(Model model, @RequestParam String subject){
        Map<String, Object> map = new HashMap<>();
        map.put("subject", subject);

        service.searchSubject(map);
        List<PreCourseDTO> openLectureList = (List<PreCourseDTO>) map.get("result");
        int count = (int) map.get("count");
        log.info("-----CourseRegistController[searchSubejct] :: openLectureList={}", openLectureList);

        model.addAttribute("openLectureList", openLectureList);

        return "pages/courseRegistration:: #open-lecture-tr";
    }

    @ResponseBody
    @PostMapping("/student/courseRegistration/preTotalRegistration")
    public String preTotalRegistration(Model model, @RequestBody List<String> preLectureList){
        log.info("-----CourseRegistController[preTotalRegistration] :: preLectureList={}", preLectureList);
        Map<String, Object> map = new HashMap<>();
        map.put("preLectureList", preLectureList);
        map.put("student", student);

        service.preTotalRegistration(map);

        List<Enrolment> registrationList = (List<Enrolment>) map.get("registrationList");
        List<String> successList = (List<String>) map.get("successList");

        log.info("-----CourseRegistController[preTotalRegistration] :: registrationList={}", registrationList);
        log.info("-----CourseRegistController[preTotalRegistration] :: successList={}", successList);

        model.addAttribute("registrationList", registrationList);
        model.addAttribute("successList",successList);

        return "pages/courseRegistration :: #pages/courseRegistration";
    }







}























