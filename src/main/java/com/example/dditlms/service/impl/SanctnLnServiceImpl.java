package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnLnProgress;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.service.SanctnLnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SanctnLnServiceImpl implements SanctnLnService {

    private final SanctnLnRepository sanctnLnRepository;

    @Override
    @Transactional
    public void updateSanctnLn(String opinion, Long userNumber, Long id) {

        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.PROCESS);

    }
}
