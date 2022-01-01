package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.PreCourseRegistrationRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.security.AccountContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PreCourseRegistrationController {

    private final PreCourseRegistrationRepository repository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;

    private String memNo;
    private String memName;
    private String memRole;

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
    }

    @GetMapping("/student/preCourseRegistration")
    public String preCourseRegistration(Model model){
        getUserInfo();
        log.info("==========preCourseRegistration=========");
        List<Major> majorList = majorRepository.findAll();

        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        List<OpenLecture> openLectureList = searchRepository.findAllByYearSeme(semester);
        List<PreCourseDTO> preCourseDTOList = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
            String id = openLecture.getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = openLecture.getLectureKind() +"";
            PreCourseDTO dto = openLecture.toDto();
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
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: 원래거 끝");

        Long studentNo = Long.parseLong(memNo);
        Student student = Student.builder()
                .userNumber(studentNo).build();

        List<PreCourseRegistration> getPreRegistrationList = repository.findByStudentNo(student);
        List<PreCourseDTO> result = new ArrayList<>();
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: getPreRegistrationList={}",getPreRegistrationList);

        for (PreCourseRegistration preCourseRegistration : getPreRegistrationList) {
            log.info("-----PreCourseRegistrationController[preCourseRegistration] :: preCourseRegistration={}",preCourseRegistration);
            String id = preCourseRegistration.getLectureCode().getId();
            String lectureClass = id.substring(id.length()-1);
            String kind = preCourseRegistration.getLectureCode().getLectureKind() +"";
            PreCourseDTO dto = preCourseRegistration.toPreDto();
            log.info("-----PreCourseRegistrationController[preCourseRegistration] :: dto={}",dto);
            dto.setLectureClass(lectureClass);
            dto.setLectureSeme(preCourseRegistration.getLectureCode().getLectureSection().getKorean());
            if (kind.equals("F")){
                kind = "오프라인 ";
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: if(kind)={}",kind);
                dto.setLecturedivision(kind);
            } else if (kind.equals("O")){
                kind = "온라인";
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: if(kind)={}",kind);
                dto.setLecturedivision(kind);
            }
            result.add(dto);
        }
        int count = result.size();
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: count={}",count);
        model.addAttribute("preRegistrationList",result);
        model.addAttribute("preTotalCount", count);

        String year = semester.getYear().split("-")[0];
        String seme = semester.getYear().split("-")[1];
        model.addAttribute("openLectureList", preCourseDTOList);
        model.addAttribute("totalCount",lectureCount);
        model.addAttribute("year",year);
        model.addAttribute("seme",seme);
        model.addAttribute("memberNo",memNo);
        model.addAttribute("memberName",memName);
        model.addAttribute("majorList",majorList);

        return "pages/preCourseRegistration";
    }

    @PostMapping("/preCourseRegistration/searchLecture")
    public String SearchLecture(Model model, @RequestParam Map<String, Object> paramMap){
        String division = (String) paramMap.get("division");
        String majorName = (String) paramMap.get("major");
        List<OpenLecture> openLectureList = null;
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        if(division.equals("total") && majorName.equals("total")){
            openLectureList = searchRepository.findAllByYearSeme(semester);
        }else if(division.equals("total")){
            Optional<Major> majorWrapper = majorRepository.findByKorean(majorName);
            Major major = majorWrapper.orElse(null);
            openLectureList = searchRepository.findAllByYearSemeAndMajorCode(semester,major);
        }else if(majorName.equals("total")){
            openLectureList = searchRepository.findAllByYearSemeAndLectureSection(semester,division);
        }else {
            Optional<Major> majorWrapper = majorRepository.findByKorean(majorName);
            Major major = majorWrapper.orElse(null);
            openLectureList = searchRepository.findAllByYearSemeAndLectureSectionAndMajorCode(semester,division,major);
        }

        List<PreCourseDTO> result = new ArrayList<>();
        for(OpenLecture openLecture : openLectureList){
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
        JSONObject jsonObject = new JSONObject();
        OpenLecture openLecture = searchRepository.findById(id).get();
        PreCourseDTO dto = openLecture.toDto();
        return dto;
    }



}
