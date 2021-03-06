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

        // ??????????????? ?????? ?????? ??????
        Long drafter = MemberUtil.getLoginMember().getUserNumber();
        Docform docform = docformRepository.findById(6L).get();
        // ?????? ????????? ???????????? ?????? ????????? ?????????, ???????????? ?????? ?????? ???????????? ?????????
        // ?????? ?????? -> ????????? ????????? ??????, ?????? ????????? ?????????????????? ????????????
        // ?????????????????? ?????? ????????? ????????????.
        List<Long> userNumber = new ArrayList<>();
        Long staff = 11126L;
        Long professor = 11127L;
        Long approver = 11130L;
        userNumber.add(staff);
        userNumber.add(professor);
        userNumber.add(approver);
        Long complimentId = scholarship.getId();
        String reason = scholarship.getKind().getKorean();
        String complimentType = "????????? ?????? : ";
        sanctnService.saveComplaint(docform, drafter, reason, userNumber, complimentId, complimentType, fileId);
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
