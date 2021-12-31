package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.sanction.Sanctn;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnLnProgress;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnLnService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SanctnLnServiceImpl implements SanctnLnService {

    private final SanctnLnRepository sanctnLnRepository;
    private final SanctnRepository sanctnRepository;

    //결재 승인
    @Override
    @Transactional
    public void updateSanctnLn(String opinion, Long userNumber, Long id) {

        // 승인자 로직 실행
        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.PROCESS);

        // 다음 결재자 요청상태로 변경하기
        SanctnLn nextSanctnId = sanctnLnRepository.findNextSanctnId(userNumber, id);
        log.info(String.valueOf(nextSanctnId));

        if(nextSanctnId != null) {

            nextSanctnId.setSanctnLnProgress(SanctnLnProgress.REQUEST);
        }
    }

    //결재 반려
    @Override
    @Transactional
    public void rejectSanctnLn(String opinion, Long userNumber, Long id) {

        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.REJECT);

        Optional<Sanctn> findSanctn = sanctnRepository.findById(id);
        findSanctn.get().setStatus(SanctnProgress.REJECT);

    }
    
    //최종 승인
    @Override
    @Transactional
    public void lastUpadteSanctnLn(String opinion, Long userNumber, Long id) {

        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.PROCESS);

        Optional<Sanctn> findSanctn = sanctnRepository.findById(id);
        findSanctn.get().setStatus(SanctnProgress.COMPLETION);


    }
}
