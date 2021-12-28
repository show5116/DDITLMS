package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.service.SemesterByYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SemesterByYearServiceImpl implements SemesterByYearService {
    private final SemesterByYearRepository semesterByYearRepository;

    @Override
    public SemesterByYear getNowSemester() {
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.findBySemeStartLessThanEqualAndSemeEndGreaterThanEqual(new Date(),new Date());
        return semesterWrapper.orElse(semesterByYearRepository.selectNextSeme().orElse(null));
    }
}
