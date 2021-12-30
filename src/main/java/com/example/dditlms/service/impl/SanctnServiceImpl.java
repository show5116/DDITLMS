package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.*;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SanctnServiceImpl implements SanctnService {

    private final SanctnRepository sanctnRepository;
    private final SanctnLnRepository sanctnLnRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void saveSanctn(String sanctnSj, Docform docform, Long drafter, String sanctnCn, List<Long> userNumber, Long attFile) {

        //트랜잭션 처리를 위해서, 한 서비스에 두 프로세스를 함께 실행함.

        //결재 저장
        Sanctn sanctn = new Sanctn();
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(7);

        sanctn.setSanctnSj(sanctnSj);
        sanctn.setSanctnCn(sanctnCn);
        sanctn.setDrafter(drafter);
        sanctn.setSanctnWritngde(now);
        sanctn.setSanctnUpdde(endDate);
        sanctn.setStatus(SanctnProgress.PROGRESS);
        Sanctn savedSanctn = sanctnRepository.save(sanctn);

        //결재라인 저장 , 일단 다 때려박아서 작성, 나중에 리팩토링 대상!
        SanctnLn sanctnLn = new SanctnLn();
        sanctnLn.setSanctnSn(savedSanctn);
        Optional<Member> byUserNumber = memberRepository.findByUserNumber(drafter);
        sanctnLn.setMberNo(byUserNumber.get());
        sanctnLn.setSanctnLnProgress(SanctnLnProgress.PROCESS);
        sanctnLn.setLastApproval("N");
        sanctnLn.setSanctnDate(LocalDateTime.now());
        sanctnLn.setSanctnStep(0);
        sanctnLnRepository.save(sanctnLn);


        if (userNumber.size() == 1) {
            SanctnLn sanctnLn1 = new SanctnLn();
            sanctnLn1.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber1 = memberRepository.findByUserNumber(userNumber.get(0));
            sanctnLn1.setMberNo(byUserNumber1.get());
            sanctnLn1.setSanctnLnProgress(SanctnLnProgress.REQUEST);
            sanctnLn1.setLastApproval("Y");
            sanctnLn1.setSanctnStep(1);
            sanctnLnRepository.save(sanctnLn1);

        } else if (userNumber.size() == 2) {
            SanctnLn sanctnLn1 = new SanctnLn();
            sanctnLn1.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber1 = memberRepository.findByUserNumber(userNumber.get(0));
            sanctnLn1.setMberNo(byUserNumber1.get());
            sanctnLn1.setSanctnLnProgress(SanctnLnProgress.REQUEST);
            sanctnLn1.setLastApproval("N");
            sanctnLn1.setSanctnStep(1);
            sanctnLnRepository.save(sanctnLn1);

            SanctnLn sanctnLn2 = new SanctnLn();
            sanctnLn2.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber2 = memberRepository.findByUserNumber(userNumber.get(1));
            sanctnLn2.setMberNo(byUserNumber2.get());
            sanctnLn2.setSanctnLnProgress(SanctnLnProgress.WAITING);
            sanctnLn2.setLastApproval("Y");
            sanctnLn2.setSanctnStep(2);
            sanctnLnRepository.save(sanctnLn2);
        } else {
            SanctnLn sanctnLn1 = new SanctnLn();
            sanctnLn1.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber1 = memberRepository.findByUserNumber(userNumber.get(0));
            sanctnLn1.setMberNo(byUserNumber1.get());
            sanctnLn1.setSanctnLnProgress(SanctnLnProgress.REQUEST);
            sanctnLn1.setLastApproval("N");
            sanctnLn1.setSanctnStep(1);
            sanctnLnRepository.save(sanctnLn1);

            SanctnLn sanctnLn2 = new SanctnLn();
            sanctnLn2.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber2 = memberRepository.findByUserNumber(userNumber.get(1));
            sanctnLn2.setMberNo(byUserNumber2.get());
            sanctnLn2.setSanctnLnProgress(SanctnLnProgress.WAITING);
            sanctnLn2.setLastApproval("N");
            sanctnLn2.setSanctnStep(2);
            sanctnLnRepository.save(sanctnLn2);

            SanctnLn sanctnLn3 = new SanctnLn();
            sanctnLn3.setSanctnSn(savedSanctn);
            Optional<Member> byUserNumber3 = memberRepository.findByUserNumber(userNumber.get(2));
            sanctnLn3.setMberNo(byUserNumber3.get());
            sanctnLn3.setSanctnLnProgress(SanctnLnProgress.WAITING);
            sanctnLn3.setLastApproval("Y");
            sanctnLn3.setSanctnStep(3);
            sanctnLnRepository.save(sanctnLn3);
        }

        // 결재 승인
                


    }
}
