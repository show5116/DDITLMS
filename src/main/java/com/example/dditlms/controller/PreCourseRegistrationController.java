package com.example.dditlms.controller;

import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.PreCourseRegistrationRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.PreCourseRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PreCourseRegistrationController {
    private final PreCourseRegistrationService service;

    private final PreCourseRegistrationRepository repository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;

    private String memNo;

    @GetMapping("/student/preCourseRegistration")
    public String preCourseRegistration(Model model){
        Map<String, Object> map = new HashMap<>();
        service.preCourseRegistration(map);

        String year = (String) map.get("year");
        String seme = (String) map.get("seme");
        memNo = (String)map.get("memNo");
        String memName = (String)map.get("memName");
        List<Major> majorList = (List<Major>) map.get("majorList");
        List<PreCourseDTO> openLectureList = (List<PreCourseDTO>) map.get("openLectureList");
        List<PreCourseDTO> preRegistrationList = (List<PreCourseDTO>) map.get("preRegistrationList");

        model.addAttribute("preRegistrationList",preRegistrationList);
        model.addAttribute("openLectureList", openLectureList);
        model.addAttribute("year",year);
        model.addAttribute("seme",seme);
        model.addAttribute("memberNo",memNo);
        model.addAttribute("memberName",memName);
        model.addAttribute("majorList",majorList);

        return "pages/preCourseRegistration";
    }

    @PostMapping("/preCourseRegistration/searchLecture")
    public String searchLecture(Model model, @RequestParam Map<String, Object> paramMap){
        String division = (String) paramMap.get("division");
        String majorName = (String) paramMap.get("major");

        Map<String, Object> map = new HashMap<>();
        map.put("division", division);
        map.put("majorName", majorName);
        service.searchLecture(map);
        List<PreCourseDTO> openLectureList = (List<PreCourseDTO>) map.get("openLectureList");

        model.addAttribute("openLectureList", openLectureList);

        return "pages/preCourseRegistration::#searchLectureList";
    }

    @PostMapping("/preCourseRegistration/searchSubject")
    public String searchSubject(Model model, @RequestParam Map<String, Object> paramMap){
        String searchSubject = (String) paramMap.get("searchSubject");
        List<OpenLecture> openLectureList = searchRepository.searchSubject(searchSubject);
        List<PreCourseDTO> result = new ArrayList<>();

        for (OpenLecture openLecture : openLectureList) {
            String id = openLecture.getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = openLecture.getLectureKind() +"";
            PreCourseDTO dto = openLecture.toDto();
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(openLecture.getLectureSection().getKorean());
            if (kind.equals("F")){
                kind = "오프라인 ";
                dto.setLecturedivision(kind);
            } else if (kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
            }
            result.add(dto);
        }
        int count = result.size();

        model.addAttribute("openLectureList", result);
        model.addAttribute("totalCount", count);

        return "pages/preCourseRegistration::#searchLectureList";
    }

    @GetMapping("/preCourseRegistration/preRegistrationList")
    public String preRegistrationList(Model model, @RequestParam String memberNo){
        log.info("-----PreCourseRegistrationController[preRegistration] :: param memberNO={}",memberNo);
        Long studentNo = Long.parseLong(memNo);
        Student student = Student.builder()
                .userNumber(studentNo).build();

        List<PreCourseRegistration> getPreRegistrationList = repository.findByStudentNo(student);
        List<PreCourseDTO> result = new ArrayList<>();
        for (PreCourseRegistration preCourseRegistration : getPreRegistrationList) {
            String id = preCourseRegistration.getLectureCode().getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = preCourseRegistration.getLectureCode().getLectureKind() +"";
            PreCourseDTO dto = preCourseRegistration.toPreDto();
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(preCourseRegistration.getLectureCode().getLectureSection().getKorean());
            if (kind.equals("F")){
                kind = "오프라인 ";
                dto.setLecturedivision(kind);
            } else if (kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
            }
            result.add(dto);
        }
        int count = result.size();
        model.addAttribute("preRegistrationList",result);
        model.addAttribute("preTotalCount", count);
        return "pages/preCourseRegistration::#preRegistrationList";
    }

    @ResponseBody
    @PostMapping("/preCourseRegistration/searchLectureId")
    public PreCourseDTO searchLectureId(@RequestParam String id){
        OpenLecture openLecture = searchRepository.findById(id).get();
        PreCourseDTO dto = openLecture.toDto();
        return dto;
    }

    @ResponseBody
    @PostMapping("/preCourseRegistration/savePreRegistration")
    public String savePreRegistration(@RequestBody List<String> idList){
        Long studentNo = Long.parseLong(memNo);
        Student student = Student.builder()
                .userNumber(studentNo).build();
        for (String lecture : idList) {
            OpenLecture addId = searchRepository.findById(lecture).get();
            PreCourseRegistration registration = PreCourseRegistration.builder()
                    .lectureCode(addId)
                    .studentNo(student)
                    .existence("0")
                    .build();
            repository.save(registration);
        }
        return "success";
    }

    @ResponseBody
    @PostMapping("/student/preCourseRegistration/deletePreRegistration")
    public String deletePreRegistration(@RequestBody String id){
        String deleteId = id.split("=")[0];
        OpenLecture openLecture = searchRepository.findById(deleteId).get();
        Long studentNo = Long.parseLong(memNo);
        Student student = Student.builder()
                .userNumber(studentNo).build();
        Optional<PreCourseRegistration> preRegistrationWrapper = repository.findByStudentNoAndLectureCode(student, openLecture);
        PreCourseRegistration preCourseRegistration = preRegistrationWrapper.orElse(null);
        if (preCourseRegistration != null){
            Long registrationId = preCourseRegistration.getId();
            repository.deleteById(registrationId);
        }

        return "success";
    }



}
