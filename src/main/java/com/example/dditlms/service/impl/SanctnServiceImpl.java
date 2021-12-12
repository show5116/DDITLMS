package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.entity.sanction.Sanctn;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void saveSanctn(String sanctnSj, Docform docform, Long drafter, String sanctnCn) {

        //트랜잭션 처리를 위해서, 한 서비스에 두 프로세스를 함께 실행함.

        //결재 저장
        Sanctn sanctn = new Sanctn();
        LocalDate now = LocalDate.now();

        sanctn.setSanctnSj(sanctnSj);
        sanctn.setSanctnCn(sanctnCn);
        sanctn.setDocform(docform);
        sanctn.setDrafter(drafter);
        sanctn.setSanctnUpdde(now);
        sanctn.setSanctnWritngde(now);
        sanctn.setStatus(SanctnProgress.PROGRESS);
        Sanctn savedSanctn = sanctnRepository.save(sanctn);

        //결재라인 저장
//        SanctnLn sanctnLn = new SanctnLn();
//        sanctnLn.setSanctnSn(savedSanctn);
//        Optional<Member> byUserNumber = memberRepository.findByUserNumber(drafter);
//        sanctnLn.setMberNo(byUserNumber.get());
//
//        sanctnLnRepository.save(sanctnLn);


    }
}
