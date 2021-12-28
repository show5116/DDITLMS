package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupSearchController {

    private final SemesterByYearRepository byYearRepository;
    private final MajorRepository majorRepository;
    private final SignupSearchRepository repository;

    @GetMapping("/signUpSearch")
    public String signUpSearch(Model model) {

        List<SemesterByYear> semesterByYearList = byYearRepository.findAll();
        List<String> yearList = new ArrayList<>();
        int i =0;
        for (SemesterByYear byYear : semesterByYearList) {
            log.info("-----SignUpSearchController[getYear]- byYear ={}", byYear);
            String years = byYear.getYear();
            log.info("-----SignUpSearchController[getYear]- years ={}", years);
            String[] split = years.split("-");
            String distinctYear = split[0];
            log.info("-----SignUpSearchController[getYear]- distinctYear ={}", distinctYear);

            if (yearList.size()==0){
                log.info("-----SignUpSearchController[getYear]- if문");
                yearList.add(distinctYear);
            }
            if (!yearList.get(i).equals(distinctYear)){
                log.info("-----SignUpSearchController[getYear]- !yearList.get(i).equals(distinctYear) ={}", !yearList.get(i).equals(distinctYear));
                yearList.add(distinctYear);
                i++;
            }
        }

        log.info("-----SignUpSearchController[getYear]- yearList ={}", yearList);

        List<SignupDTO> openLectures = repository.totalLectureList();

        model.addAttribute("yearList", yearList);
        model.addAttribute("openLectures", openLectures);

        return "pages/signUpSearch";
    }

    @PostMapping("/signUpSearch/getMajor")
    public void getMajor(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        log.info("-----SignupSearchController-getMajor");

        JSONObject jsonObject = new JSONObject();

        String college = (String) paramMap.get("selectValue");
        log.info("-----SignupSearchController-getMajor :: college = {}", college);
        List<Major> collegeMajorList = majorRepository.collegeMajorList(college);
        log.info("-----SignupSearchController-getMajor :: majorList = {}", collegeMajorList);
        log.info(collegeMajorList.toString());
        List<String> majorList = new ArrayList<>();
        List<String> majorCode = new ArrayList<>();
        for (Major major : collegeMajorList) {
            log.info(major.getKorean());
            log.info(major.getId());
            majorList.add(major.getKorean());
            majorCode.add(major.getId());
        }

        jsonObject.put("majorList", majorList);
        jsonObject.put("majorCode", majorCode);


        try{
            response.getWriter().print(jsonObject.toJSONString());
        }catch (IOException e){}

    }

    @PostMapping("/signUpSearch/searchSubject")
    public String searchSubject(Model model, @RequestParam Map<String, Object> paramMap){



        return "pages/signUpSearch";
    }










}
