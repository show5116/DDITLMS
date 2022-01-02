package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.*;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
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
    }


    //민원 신청, 우선은 휴학신청만 구현, 나중에는 파라미터 값을 이용해서 해당하는 민원 서식 신청 하도록 구현 할 것!
    @Override
    public void saveComplaint(Docform docform, Long drafter, String sanctnCn, List<Long> userNumber) {

        String complainant = MemberUtil.getLoginMember().getName();

        //민원 저장
        Sanctn sanctn = new Sanctn();
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(7);

        sanctn.setSanctnSj("휴학신청 : " + complainant);
        sanctn.setSanctnCn(sanctnCn);
        sanctn.setDrafter(drafter);
        sanctn.setSanctnWritngde(now);
        sanctn.setSanctnUpdde(endDate);
        sanctn.setStatus(SanctnProgress.PROGRESS);
        Sanctn savedSanctn = sanctnRepository.save(sanctn);

        // 민원 결재선 저장(신청자 본인)

        SanctnLn sanctnLn = new SanctnLn();
        sanctnLn.setSanctnSn(savedSanctn);
        Optional<Member> byUserNumber = memberRepository.findByUserNumber(drafter);
        sanctnLn.setMberNo(byUserNumber.get());
        sanctnLn.setSanctnLnProgress(SanctnLnProgress.PROCESS);
        sanctnLn.setLastApproval("N");
        sanctnLn.setSanctnDate(LocalDateTime.now());
        sanctnLn.setSanctnStep(0);
        sanctnLnRepository.save(sanctnLn);

        // 민원 결재선 저장(담당직원)
        SanctnLn sanctnLn1 = new SanctnLn();
        sanctnLn1.setSanctnSn(savedSanctn);
        Optional<Member> byUserNumber1 = memberRepository.findByUserNumber(userNumber.get(0));
        sanctnLn1.setMberNo(byUserNumber1.get());
        sanctnLn1.setSanctnLnProgress(SanctnLnProgress.REQUEST);
        sanctnLn1.setLastApproval("N");
        sanctnLn1.setSanctnStep(1);
        sanctnLnRepository.save(sanctnLn1);

        // 민원 결재선 저장(담당교수)
        SanctnLn sanctnLn2 = new SanctnLn();
        sanctnLn2.setSanctnSn(savedSanctn);
        Optional<Member> byUserNumber2 = memberRepository.findByUserNumber(userNumber.get(1));
        sanctnLn2.setMberNo(byUserNumber2.get());
        sanctnLn2.setSanctnLnProgress(SanctnLnProgress.WAITING);
        sanctnLn2.setLastApproval("N");
        sanctnLn2.setSanctnStep(1);
        sanctnLnRepository.save(sanctnLn2);

        // 민원 결재선 저장(최종승인 직원)
        SanctnLn sanctnLn3 = new SanctnLn();
        sanctnLn3.setSanctnSn(savedSanctn);
        Optional<Member> byUserNumber3 = memberRepository.findByUserNumber(userNumber.get(2));
        sanctnLn3.setMberNo(byUserNumber3.get());
        sanctnLn3.setSanctnLnProgress(SanctnLnProgress.WAITING);
        sanctnLn3.setLastApproval("Y");
        sanctnLn3.setSanctnStep(1);
        sanctnLnRepository.save(sanctnLn3);

    }
    
    
    //학생 민원신청 결과 반환
    @Override
    public Optional<SanctnDTO> viewComplaint(Long id) {

        Map<String, Object> resultList = null;
        Optional<Map<String, Object>> result = Optional.ofNullable(sanctnLnRepository.viewCompliment(id));
        SanctnDTO sanctnDTO = new SanctnDTO();
        
        if (!result.get().isEmpty()) {



            resultList = result.get();
            Timestamp beforeConvertDate = (Timestamp) resultList.get("SANCTN_DATE");
            LocalDateTime localDateTime = beforeConvertDate.toLocalDateTime();
            Integer sanctn_step = Integer.valueOf(resultList.get("SANCTN_STEP").toString());
            String sanctn_ls_apv = resultList.get("SANCTN_LS_APV").toString();
            String mber_nm = resultList.get("MBER_NM").toString();
            Long mber_no = Long.valueOf(resultList.get("MBER_NO").toString());
            String major_nm_kr = resultList.get("MAJOR_NM_KR").toString();
            String nowPro = resultList.get("SANCTN_STTUS").toString();

            switch (nowPro) {
                case "DRAFTER":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.DRAFTER);
                    break;

                case "PROCESS":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.PROCESS);
                    break;
                case "WAITING":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.WAITING);
                    break;
                case "REJECT":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.REJECT);
                    break;
                case "REQUEST":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.REQUEST);
                    break;

            }
            sanctnDTO.setSanctnDate(localDateTime);
            sanctnDTO.setSanctnStep(sanctn_step);
            sanctnDTO.setLastApproval(sanctn_ls_apv);
            sanctnDTO.setName(mber_nm);
            sanctnDTO.setUserNumber(mber_no);
            sanctnDTO.setMajor_nm_kr(major_nm_kr);

        }
        log.info("옵셔널 반환 값 확인" + String.valueOf(sanctnDTO));
        return Optional.of(sanctnDTO);
    }

    @Override
    public Optional<SanctnDTO> viewComplaintPro(Long id) {

        Map<String, Object> resultList = null;
        Optional<Map<String, Object>> result = Optional.ofNullable(sanctnLnRepository.viewComplimentPro(id));
        SanctnDTO sanctnDTO = new SanctnDTO();


        if (!result.get().isEmpty()) {

            resultList = result.get();

            Timestamp beforeConvertDate = (Timestamp) resultList.get("SANCTN_DATE");
            if (beforeConvertDate !=null) {
                LocalDateTime localDateTime = beforeConvertDate.toLocalDateTime();
                sanctnDTO.setSanctnDate(localDateTime);
            }
            Integer sanctn_step = Integer.valueOf(resultList.get("SANCTN_STEP").toString());
            String sanctn_ls_apv = resultList.get("SANCTN_LS_APV").toString();
            String mber_nm = resultList.get("MBER_NM").toString();
            Long mber_no = Long.valueOf(resultList.get("MBER_NO").toString());
            String major_nm_kr = resultList.get("MAJOR_NM_KR").toString();
            String nowPro = resultList.get("SANCTN_STTUS").toString();

            switch (nowPro) {
                case "DRAFTER":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.DRAFTER);
                    break;

                case "PROCESS":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.PROCESS);
                    break;
                case "WAITING":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.WAITING);
                    break;
                case "REJECT":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.REJECT);
                    break;
                case "REQUEST":
                    sanctnDTO.setSanctnLnProgress(SanctnLnProgress.REQUEST);
                    break;

            }
            sanctnDTO.setSanctnStep(sanctn_step);
            sanctnDTO.setLastApproval(sanctn_ls_apv);
            sanctnDTO.setName(mber_nm);
            sanctnDTO.setUserNumber(mber_no);
            sanctnDTO.setMajor_nm_kr(major_nm_kr);

        }

        return Optional.of(sanctnDTO);
    }


}

