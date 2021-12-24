package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupSearchController {

    private final SemesterByYearRepository byYearRepository;

    @GetMapping("/signUpSearch")
    public String signUpSearch() {return "pages/signUpSearch";}

    @GetMapping("/signUpSearch/getYear")
    public void getYear(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");

        JSONObject jsonObject = new JSONObject();

        List<SemesterByYear> semesterByYearList = byYearRepository.findAll();
        List<String> yearList = new ArrayList<>();

        for (SemesterByYear byYear : semesterByYearList) {
            String years = byYear.getYear();
            for (String year : yearList) {
                String[] split = years.split("-");
                String distinctYear = split[0];

                if (!year.equals(distinctYear)){
                    yearList.add(year);
                }
            }
        }

        log.info("-----SignUpSearchController[getYear]- yearList ={}", yearList);

        jsonObject.put("yearList",yearList);

        try{
            response.getWriter().print(jsonObject.toJSONString());
        }catch (IOException e){}

    }












}
