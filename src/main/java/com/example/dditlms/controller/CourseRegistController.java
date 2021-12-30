package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.PreCourseDTO;
import com.example.dditlms.domain.entity.OpenLecture;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SignupSearchRepository;
import com.example.dditlms.service.SemesterByYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CourseRegistController {
    private final SignupSearchRepository searchRepository;

    private final SemesterByYearRepository semesterByYearRepository;


    @GetMapping("/courseRegistration")
    public String courseRegistration() {return "pages/courseRegistration";}



}
