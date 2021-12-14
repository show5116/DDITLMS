package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Student;

import java.util.List;

public interface CalendarRepositoryCustom {

    public Student getMajor(Long userNumber);

    public List<Calendar> getAllScheduleList(Member userNumber, String scheduleType);



}
