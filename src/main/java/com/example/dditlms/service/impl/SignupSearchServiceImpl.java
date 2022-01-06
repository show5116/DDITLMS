package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.service.SignupSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupSearchServiceImpl implements SignupSearchService {
    private final SemesterByYearRepository byYearRepository;
    private final MajorRepository majorRepository;
    private final SignupSearchRepository repository;

    @Override
    @Transactional
    public void signUpSearch(Map<String, Object> map){
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

        List<Major> majorList = majorRepository.findAll();

        map.put("majorList", majorList);
        map.put("yearList", yearList);
        map.put("openLectures", openLectures);
        map.put("totalCount", count);
    }

    @Override
    @Transactional
    public void getMajor(Map<String, Object> map){
        String college = (String) map.get("college");
        List<Major> majorList =  new ArrayList<>();

        if (college.equals("total")){
            majorList = majorRepository.findAll();
        } else {
            majorList = majorRepository.collegeMajorList(college);
        }
        map.put("majorList", majorList);
    }



    @Override
    @Transactional
    public void allAutoSearch(Map<String, Object> map){
        String year = (String) map.get("year");
        String seme = (String) map.get("seme");
        String college = (String) map.get("college");
        String major = (String) map.get("major");
        String division = (String) map.get("division");

        log.info("-------------SERVICE[allAutoSearch] :: college = {}",  college);
        log.info("-------------SERVICE[allAutoSearch] :: major = {}",  major);

        Map<String,Object> search = new HashMap<>();
        search.put("name", "allAutoSearch");
        search.put("searchYear",year);
        search.put("searchSeme",seme);
        search.put("college",college);
        search.put("division",division);
        search.put("major",major);

        List<SignupDTO> result = repository.totalLectureList(search);

        map.put("result", result);
    }
    
    
    

}
