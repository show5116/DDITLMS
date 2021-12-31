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
import java.util.HashMap;
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
            String years = byYear.getYear();
            String[] split = years.split("-");
            String distinctYear = split[0];
            if (yearList.size()==0){
                yearList.add(distinctYear);
            }
            if (!yearList.get(i).equals(distinctYear)){
                yearList.add(distinctYear);
                i++;
            }
        }
        Map<String,Object> search = new HashMap<>();
        search.put("name","totalList");
        List<SignupDTO> openLectures = repository.totalLectureList(search);
        int count = openLectures.size();

        model.addAttribute("yearList", yearList);
        model.addAttribute("openLectures", openLectures);
        model.addAttribute("totalCount",count);
        return "pages/signUpSearch";
    }

    @PostMapping("/signUpSearch/getMajor")
    public void getMajor(HttpServletResponse response, @RequestParam Map<String, Object> paramMap){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        String college = (String) paramMap.get("selectValue");
        List<Major> collegeMajorList = majorRepository.collegeMajorList(college);
        List<String> majorList = new ArrayList<>();
        List<String> majorCode = new ArrayList<>();
        for (Major major : collegeMajorList) {
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
        String searchSubject = (String) paramMap.get("subject");
        Map<String,Object> search = new HashMap<>();
        search.put("name","searchSubject");
        search.put("subject", searchSubject);
        List<SignupDTO> openLectures = repository.totalLectureList(search);
        model.addAttribute("openLectures",openLectures);
        return "pages/signUpSearch::#list";
    }

    @PostMapping("/signUpSearch/searchYear")
    public String searchYear(Model model, @RequestParam Map<String, Object> paramMap){
        String searchYear = (String) paramMap.get("selectYear");
        String getSearchSeme = (String) paramMap.get("selectSeme");
        String[] division = getSearchSeme.split("학기");
        String searchSeme = division[0];

        Map<String, Object> search = new HashMap<>();
        search.put("name", "searchYear");
        search.put("searchYear",searchYear);
        search.put("searchSeme",searchSeme);
        List<SignupDTO> result = repository.totalLectureList(search);

        model.addAttribute("openLectures", result);

        return "pages/signUpSearch::#list";
    }

    @PostMapping("/signUpSearch/searchMajor")
    public String searchMajor(Model model, @RequestParam Map<String, Object> paramMap){
        String college = (String) paramMap.get("college");
        String major =(String)paramMap.get("major");

        Map<String,Object> search = new HashMap<>();
        search.put("name", "searchMajor");
        search.put("college",college);
        if (major.equals("선택")){
            major = "";
            search.put("major",major);
        } else {
            search.put("major",major);
        }
        List<SignupDTO> result = repository.totalLectureList(search);

        model.addAttribute("openLectures", result);

        return "pages/signUpSearch::#list";
    }

    @PostMapping("/signUpSearch/searchCollege")
    public String searchCollege(Model model, @RequestParam Map<String, Object> paramMap){
        String college = (String) paramMap.get("college");
        Map<String,Object> search = new HashMap<>();
        search.put("name", "searchCollege");
        search.put("college",college);

        List<SignupDTO> result = repository.totalLectureList(search);

        model.addAttribute("openLectures", result);

        return "pages/signUpSearch::#list";
    }

    @PostMapping("/signUpSearch/allAutoSearch")
    public String allAutoSearch(Model model, @RequestParam Map<String, Object> paramMap){
        String searchYear = (String) paramMap.get("searchYear");
        String getSearchSeme = (String) paramMap.get("searchSeme");
        String[] division = getSearchSeme.split("학기");
        String searchSeme = division[0];
        String college = (String) paramMap.get("searchCollege");
        String major =(String)paramMap.get("searchMajor");
        String searchdivision = (String)paramMap.get("searchdivision");

        Map<String,Object> search = new HashMap<>();
        search.put("name", "allAutoSearch");
        search.put("searchYear",searchYear);
        search.put("searchSeme",searchSeme);
        search.put("college",college);
        search.put("division",searchdivision);
        search.put("major",major);

        List<SignupDTO> result = repository.totalLectureList(search);
        int count = result.size();

        model.addAttribute("openLectures", result);
        model.addAttribute("totalCount", count);

        return "pages/signUpSearch::#list";
    }
}