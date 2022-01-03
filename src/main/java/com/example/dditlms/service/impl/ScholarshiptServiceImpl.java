package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.repository.ScholarshipKindRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
import com.example.dditlms.domain.repository.sanctn.DocformRepository;
import com.example.dditlms.service.SanctnService;
import com.example.dditlms.service.ScholarshipService;
import com.example.dditlms.service.SemesterByYearService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScholarshiptServiceImpl implements ScholarshipService {
    private final ScholarshipKindRepository scholarshipKindRepository;

    private final ScholarshipRepository scholarshipRepository;

    private final SemesterByYearService semesterByYearService;

    private final DocformRepository docformRepository;

    private final SanctnService sanctnService;



    @Override
    public void addScholarship(long fileId,String kindId) {
        Optional<ScholarshipKind> scholarshipKindWrapper = scholarshipKindRepository.findById(kindId);
        ScholarshipKind scholarshipKind = scholarshipKindWrapper.orElse(null);
        SemesterByYear semester = semesterByYearService.getNowSemester();
        Student student = MemberUtil.getLoginMember().getStudent();
        ScholarshipMethod method = null;

        long cost = 0;
        List<Scholarship> scholarshipList =
                scholarshipRepository.findAllByStudentAndSemesterAndStatusNot(student,semester, ResultStatus.COMPANION);
        for(Scholarship scholarship : scholarshipList){
            cost += scholarship.getPrice();
        }
        System.out.println(student.getMajor().getPayment());
        if(student.getMajor().getPayment() < cost){
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
                .student(student)
                .attachment(fileId)
                .status(ResultStatus.STANDBY).build();
        scholarshipRepository.save(scholarship);

        // 전자결재에 민원 추가 로직
        Long drafter = MemberUtil.getLoginMember().getUserNumber();
        Docform docform = docformRepository.findById(6L).get();
        // 담당 직원과 최종승인 직원 강제로 삽입함, 실제로는 각각 검색 메소드가 필요함
        // 실제 로직 -> 장학금 종류에 따라, 담당 직원과 최종승인자를 검색해서
        // 전자결재선에 해당 사람을 추가한다.
        List<Long> userNumber = new ArrayList<>();
        Long staff = 11111L;
        Long professor = 8888L;
        Long approver = 11112L;
        userNumber.add(staff);
        userNumber.add(professor);
        userNumber.add(approver);
        Long complimentId = scholarship.getId();
        String reason = scholarship.getKind().getKorean();
        String complimentType = "장학금 신청 : ";
        sanctnService.saveComplaint(docform, drafter, reason, userNumber, complimentId, complimentType);
    }

    @Override
    public void completeScholarship(Scholarship scholarship) {
        scholarship.setCompleteDate(new Date());
        scholarship.setStatus(ResultStatus.APPROVAL);
        scholarshipRepository.save(scholarship);
    }

    @Override
    public void companionScholarship(Scholarship scholarship) {
        scholarship.setCompleteDate(new Date());
        scholarship.setStatus(ResultStatus.COMPANION);
        scholarshipRepository.save(scholarship);
    }

}
