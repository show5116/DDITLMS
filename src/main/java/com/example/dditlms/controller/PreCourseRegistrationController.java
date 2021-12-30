package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.PreCourseRegistrationRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.security.AccountContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PreCourseRegistrationController {

    private final PreCourseRegistrationRepository repository;
    private final SignupSearchRepository signupSearchRepository;
    private final SignupSearchRepository searchRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final MajorRepository majorRepository;


    @GetMapping("/preCourseRegistration")
    public String preCourseRegistration(Model model){
        log.info("==========preCourseRegistration=========");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member =null;
        try{
            member = ((AccountContext)authentication.getPrincipal()).getMember();
        }catch(ClassCastException e){
        }

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

            if (kind.equals("F")){
                kind = "오프라인";
                dto.setLecturedivision(kind);
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: kind ={}", kind);
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: dto.getLecturedivision ={}", dto.getLecturedivision());
            } else if(kind.equals("O")){
                kind = "온라인";
                dto.setLecturedivision(kind);
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: kind  ={}", kind);
                log.info("-----PreCourseRegistrationController[preCourseRegistration] :: dto.getLecturedivision ={}", dto.getLecturedivision());
            }

            log.info("-----PreCourseRegistrationController[preCourseRegistration] :: lectureClass ={}", lectureClass);
            log.info("-----PreCourseRegistrationController[preCourseRegistration] :: dto.getLectureClass ={}", dto.getLectureClass());
            preCourseDTOList.add(dto);
        }


        int count = preCourseDTOList.size();
        String year = semester.getYear().split("-")[0];
        String seme = semester.getYear().split("-")[1];
        String memberNo = member.getUserNumber() +"";
        String memberName = member.getName();

        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: semester ={}", semester.getYear());
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: openLectureList ={}", openLectureList);
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: preCourseDTOList ={}", preCourseDTOList);
        log.info("-----PreCourseRegistrationController[preCourseRegistration] :: count ={}", count);

        model.addAttribute("opendLectureList", preCourseDTOList);
        model.addAttribute("totalCount",count);
        model.addAttribute("year",year);
        model.addAttribute("seme",seme);
        model.addAttribute("memberNo",memberNo);
        model.addAttribute("memberName",memberName);
        model.addAttribute("majorList",majorList);

        return "pages/preCourseRegistration";
    }




}
