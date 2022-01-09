package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.service.SignupSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupSearchController {
    private final SignupSearchService service;

    private final SignupSearchRepository repository;

    @GetMapping("/signUpSearch")
    public ModelAndView signUpSearch(ModelAndView mav) {
        Map<String, Object> map = new HashMap<>();
        service.signUpSearch(map);

        List<String> yearList = (List<String>) map.get("yearList");
        List<SignupDTO> openLectures = (List<SignupDTO>) map.get("openLectures");
        int count = (int) map.get("totalCount");
        List<Major> majorList = (List<Major>) map.get("majorList");

        mav.addObject("majorList", majorList);
        mav.addObject("yearList", yearList);
        mav.addObject("openLectures", openLectures);
        mav.addObject("totalCount",count);

        mav.setViewName("pages/signUpSearch");
        return mav;
    }

    @PostMapping("/signUpSearch/getMajor")
    public ModelAndView getMajor(ModelAndView mav, @RequestParam Map<String, Object> paramMap){
        String college = (String) paramMap.get("selectValue");
        Map<String, Object> map = new HashMap<>();
        map.put("college", college);
        service.getMajor(map);
        List<Major> majorList = (List<Major>) map.get("majorList");

        mav.addObject("majorList", majorList);
        log.info("-----Controller[getMajor] :: majorList={}",majorList);
        mav.setViewName("pages/signUpSearch::#department");
        return mav;
    }

    @PostMapping("/signUpSearch/searchSubject")
    public String searchSubject(Model model, @RequestParam Map<String, Object> paramMap){
        String searchSubject = (String) paramMap.get("subject");
        Map<String,Object> search = new HashMap<>();
        search.put("name","searchSubject");
        search.put("subject", searchSubject);
        List<SignupDTO> openLectures = repository.totalLectureList(search);
        model.addAttribute("openLectures",openLectures);
        return "pages/signUpSearch::#list";
    }


    @PostMapping("/signUpSearch/allAutoSearch")
    public String allAutoSearch(Model model, @RequestParam Map<String, Object> paramMap){
        log.info("-----------------CONTROLLER[allAutoSearch] :: ");
        String searchYear = (String) paramMap.get("searchYear");
        String getSearchSeme = (String) paramMap.get("searchSeme");
        String searchSeme = getSearchSeme.split("학기")[0];
        String college = (String) paramMap.get("searchCollege");
        String major =(String)paramMap.get("searchMajor");
        String searchdivision = (String)paramMap.get("searchdivision");

        log.info("--------CONTROLLER[allAutoSearch] :: major = {}", major);

        Map<String, Object> map = new HashMap<>();
        map.put("year",searchYear);
        map.put("seme", searchSeme);
        map.put("college", college);
        map.put("major", major);
        map.put("division", searchdivision);
        service.allAutoSearch(map);

        List<SignupDTO> result = (List<SignupDTO>) map.get("result");
        log.info("-----------------CONTROLLER[allAutoSearch] :: result = {}", result);

        model.addAttribute("openLectures", result);

        return "pages/signUpSearch::#list";
    }
}