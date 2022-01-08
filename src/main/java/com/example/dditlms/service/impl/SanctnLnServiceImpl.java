package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.TempAbsence;
import com.example.dditlms.domain.entity.sanction.Sanctn;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnLnProgress;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
import com.example.dditlms.domain.repository.TempAbsenceRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnLnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SanctnLnServiceImpl implements SanctnLnService {

    private final SanctnLnRepository sanctnLnRepository;
    private final SanctnRepository sanctnRepository;
    private final HistoryRepository histRepository;
    private final TempAbsenceRepository tempAbsenceRepository;
    private final ScholarshipRepository scholarshipRepository;

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

        if (nextSanctnId != null) {

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

    //민원 반려
    @Override
    @Transactional
    public void rejectComplement(String opinion, Long userNumber, Long id, Long comId) {

        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);

        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.REJECT);

        Optional<Sanctn> findSanctn = sanctnRepository.findById(id);
        findSanctn.get().setStatus(SanctnProgress.REJECT);

        Optional<History> findId = histRepository.findById(comId);

        if (findId.isPresent()) {

            History history = histRepository.findById(comId).get();
            history.setChangeDate(new Date());
            history.setResultStatus(ResultStatus.COMPANION);

            TempAbsence tempAbsence = tempAbsenceRepository.findById(comId).get();
            tempAbsence.setStatus(ResultStatus.COMPANION);
        }

        Optional<Scholarship> findId2 = scholarshipRepository.findById(comId);

        if (findId2.isPresent()) {
            Scholarship scholarship = findId2.get();
            scholarship.setCompleteDate(new Date());
            scholarship.setStatus(ResultStatus.COMPANION);
        }

    }

    //최종 승인
    @Override
    @Transactional
    public void lastUpdateSanctnLn(String opinion, Long userNumber, Long id) {

        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.PROCESS);

        Optional<Sanctn> findSanctn = sanctnRepository.findById(id);
        findSanctn.get().setStatus(SanctnProgress.COMPLETION);
    }

    //민원 최종승인
    @Override
    @Transactional
    public void lastUpdateComplement(String opinion, Long userNumber, Long id, Long comId) {
        SanctnLn sanctnId = sanctnLnRepository.findSanctnId(userNumber, id);
        sanctnId.setSanctnOpinion(opinion);
        sanctnId.setSanctnDate(LocalDateTime.now());
        sanctnId.setSanctnLnProgress(SanctnLnProgress.PROCESS);

        Optional<Sanctn> findSanctn = sanctnRepository.findById(id);
        findSanctn.get().setStatus(SanctnProgress.COMPLETION);

        Optional<History> findId = histRepository.findById(comId);
        if (findId.isPresent()) {

            History history = histRepository.findById(comId).get();
            history.setChangeDate(new Date());
            history.setResultStatus(ResultStatus.APPROVAL);

            TempAbsence tempAbsence = tempAbsenceRepository.findById(comId).get();
            tempAbsence.setStatus(ResultStatus.APPROVAL);
        }

        Optional<Scholarship> findId2 = scholarshipRepository.findById(comId);

        if (findId2.isPresent()) {
            Scholarship scholarship = findId2.get();
            scholarship.setCompleteDate(new Date());
            scholarship.setStatus(ResultStatus.APPROVAL);
        }

    }

}
