package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.dto.SignupDTO;
import com.example.dditlms.domain.entity.Attachment;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.*;
import com.example.dditlms.service.SignupSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupSearchServiceImpl implements SignupSearchService {
    private final SemesterByYearRepository byYearRepository;
    private final MajorRepository majorRepository;
    private final SignupSearchRepository repository;
    private final EnrolmentRepository enrolmentRepository;
    private final AttachmentRepository attachmentRepository;

    @Override
    @Transactional
    public void signUpSearch(Map<String, Object> map){
        Optional<SemesterByYear> semesterWrapper = byYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        String semeYear = semester.getYear().split("-")[0];
        int parseSemeYear = Integer.parseInt(semeYear);

        List<SemesterByYear> semesterByYearList = byYearRepository.findAll();
        List<String> yearList = new ArrayList<>();
        int i =0;
        for (SemesterByYear byYear : semesterByYearList) {
            String years = byYear.getYear();
            String[] split = years.split("-");
            String distinctYear = split[0];
            int parseYear = Integer.parseInt(distinctYear);
            if (parseYear > parseSemeYear){
            }else {
                if (yearList.size()==0){
                    yearList.add(distinctYear);
                }
                if (!yearList.get(i).equals(distinctYear)){
                    yearList.add(distinctYear);
                    i++;
                }
            }
        }

        Map<String,Object> search = new HashMap<>();
        search.put("name","totalList");
        List<SignupDTO> openLectures = repository.totalLectureList(search);
        List<SignupDTO> result = new ArrayList<>();
        for (SignupDTO dto : openLectures){
            int fileId = dto.getFileId();
            Attachment attachment = attachmentRepository.findByIdAndOrder(Long.valueOf(fileId),1).get();
            dto.setFilePath(attachment.getOriginName());
            result.add(dto);
        }
        log.info("-----serviceImpl :: result = {}", result);

        List<Major> majorList = majorRepository.findAll();

        map.put("majorList", majorList);
        map.put("yearList", yearList);
        map.put("openLectures", result);
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

        Map<String,Object> search = new HashMap<>();
        search.put("name", "allAutoSearch");
        search.put("searchYear",year);
        search.put("searchSeme",seme);
        search.put("college",college);
        search.put("division",division);
        search.put("major",major);

        List<SignupDTO> searchLecture = repository.totalLectureList(search);
        List<SignupDTO> result = new ArrayList<>();
        for (SignupDTO dto : searchLecture){
            int fileId = dto.getFileId();
            Attachment attachment = attachmentRepository.findByIdAndOrder(Long.valueOf(fileId),1).get();
            dto.setFilePath(attachment.getOriginName());
            result.add(dto);
        }
        map.put("result", result);
    }

    @Override
    @Transactional
    public void getLectureManageList(Map<String, Object> map){
        SemesterByYear semester = byYearRepository.selectNextSeme().orElse(null);
        List<Major> majorList = majorRepository.findAll();
        List<OpenLecture> lectureList = repository.findAllByYearSeme(semester);
        for (OpenLecture lecture : lectureList){
            int applicants = enrolmentRepository.countEnrolmentByOpenLecture(lecture);

        }


        map.put("lectureList",lectureList);
        map.put("majorList", majorList);

    }
    

}
