package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Student;

import java.util.List;

public interface CalendarRepositoryCustom {

    // 해당 회원번호를 가진 사람의 전공찾기
    public Student getMajor(Long userNumber);

    // 해당 회원번호를 가진 사람이 해당되는 모든 일정 출력
    public List<Calendar> getAllScheduleList(Member userNumber);

    // 삭제하려는 일정이 본인이 등록한 일정이 맞는지 확인 (1이면 본인꺼, 0이면 본인등록X)
    public int countConfirmScheduleWriter(Calendar calendar);

    //학과 리스트
    public List<String> getAllMajorList();

    public Long getLastScheduleNumber();

    public Calendar getSchedule(Long id);


}
