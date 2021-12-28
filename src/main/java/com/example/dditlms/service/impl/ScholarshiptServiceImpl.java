package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.common.ScholarshipStatus;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.ScholarshipKind;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.repository.ScholarshipKindRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.service.ScholarshipService;
import com.example.dditlms.service.SemesterByYearService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScholarshiptServiceImpl implements ScholarshipService {
    private final ScholarshipKindRepository scholarshipKindRepository;

    private final ScholarshipRepository scholarshipRepository;

    private final SemesterByYearService semesterByYearService;

    @Override
    public void addScholarship(long fileId,String kindId) {
        Optional<ScholarshipKind> scholarshipKindWrapper = scholarshipKindRepository.findById(kindId);
        ScholarshipKind scholarshipKind = scholarshipKindWrapper.orElse(null);
        SemesterByYear semester = semesterByYearService.getNowSemester();
        Member member = MemberUtil.getLoginMember();
        ScholarshipMethod method = null;

        long cost = 0;
        List<Scholarship> scholarshipList =
                scholarshipRepository.findAllByMemberAndSemesterAndStatusNot(member,semester,ScholarshipStatus.COMPANION);
        for(Scholarship scholarship : scholarshipList){
            cost += scholarship.getPrice();
        }
        System.out.println(member.getStudent().getMajor().getPayment());
        if(member.getStudent().getMajor().getPayment() < cost){
            method = ScholarshipMethod.PAYMENTS;
        }else{
            method = ScholarshipMethod.REDUCTION;
        }
        Scholarship scholarship = Scholarship.builder()
                .price(scholarshipKind.getPrice())
                .kind(scholarshipKind)
                .method(method)
                .apliDate(new Date())
                .semester(semester)
                .member(member)
                .attachment(fileId)
                .status(ScholarshipStatus.STANDBY).build();
        scholarshipRepository.save(scholarship);
        //결제로직 추가
    }

    @Override
    public void completeScholarship(Scholarship scholarship) {
        scholarship.setCompleteDate(new Date());
        scholarship.setStatus(ScholarshipStatus.APPROVAL);
        scholarshipRepository.save(scholarship);
    }

    @Override
    public void companionScholarship(Scholarship scholarship) {
        scholarship.setCompleteDate(new Date());
        scholarship.setStatus(ScholarshipStatus.COMPANION);
        scholarshipRepository.save(scholarship);
    }

}
