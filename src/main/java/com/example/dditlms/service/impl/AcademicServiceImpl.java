package com.example.dditlms.service.impl;

import com.example.dditlms.controller.FileDataController;
import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import com.example.dditlms.domain.entity.TempAbsence;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.TempAbsenceRepository;
import com.example.dditlms.service.AcademicService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);
    private final HistoryRepository histRepository;
    private final TempAbsenceRepository tempAbsenceRepository;
    private final SemesterByYearRepository semesterByYearRepository;

    @Transactional
    @Override
    public void applicationStatus(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findByMberNoAndStatus(student, ResultStatus.STANDBY);
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);

        map.put("student", student);
        map.put("history", history);
        map.put("tempAbsence", tempAbsence);


    }

    @Transactional
    @Override
    public void historyInsert(Map<String, Object> map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Student student = MemberUtil.getLoginMember().getStudent();

        String startDate = request.getParameter("start-date");
        String term = request.getParameter("leave-term");
        String reason = request.getParameter("reason");

        // 휴학사유 + 휴학하는 학기 + 결재 진행상황 + 첨부파일(얘는 결재쪽)
        History history = History.builder()
                .aplicationDate(new Date())
                .status(AcademicStatus.TAKEABREAK)
                .resultStatus(ResultStatus.STANDBY)
                .note(reason)
                .student(student)
                .build();
        histRepository.save(history);

        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.findById(startDate);
        SemesterByYear semesterByYear = semesterWrapper.orElse(null);

    }

    @Transactional
    @Override
    public void semesterInsert(Map<String, Object> map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        Student student = MemberUtil.getLoginMember().getStudent();

        String startDate = request.getParameter("start-date");
        String endDate = request.getParameter("start-date");
        String term = request.getParameter("leave-term");
        int yy = Integer.parseInt(endDate.split("-")[0]);

        if(term.equals("1")){
            logger.info("1입니다.");
            if(endDate.substring(endDate.length()-1).equals("1")){
                logger.info("현재 1학기입니다.");
                endDate = yy + "-2";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                logger.info("현재 2학기입니다.");
                endDate = (yy+1) + "-1";
            }
        } else if(term.equals("2")) {
            logger.info("2입니다.");
            if(endDate.substring(endDate.length()-1).equals("1")){
                logger.info("현재 1학기입니다.");
                endDate = (yy+1) + "-1";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                logger.info("현재 2학기입니다.");
                endDate = (yy+1) + "-2";
            }
        } else if(term.equals("3")){
            logger.info("3입니다.");
            if(endDate.substring(endDate.length()-1).equals("1")){
                logger.info("현재 1학기입니다.");
                endDate = (yy+1) + "-2";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                logger.info("현재 2학기입니다.");
                endDate = (yy+2) + "-1";
            }
        } else if(term.equals("4")){
            logger.info("4입니다.");
            if(endDate.substring(endDate.length()-1).equals("1")){
                logger.info("현재 1학기입니다.");
                endDate = (yy+2) + "-1";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                logger.info("현재 2학기입니다.");
                endDate = (yy+2) + "-2";
            }
        }

        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.findById(startDate);
        SemesterByYear semesterByYear = semesterWrapper.orElse(null);

        Optional<SemesterByYear> semesterEndWrapper = semesterByYearRepository.findById(endDate);
        SemesterByYear semesterByYearEnd = semesterEndWrapper.orElse(null);


        // 여기선 휴학 신청테이블을 만들어야지'
        TempAbsence tempAbsence = TempAbsence.builder()
                .mberNo(student)
                .beginsemstr(semesterByYear)
                .endsemstr(semesterByYearEnd)
                .status(ResultStatus.STANDBY)
                .build();
        tempAbsenceRepository.save(tempAbsence);

    }


    @Transactional
    @Override
    public void cancelService(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findByMberNoAndStatus(student, ResultStatus.STANDBY);
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);
        tempAbsenceRepository.delete(tempAbsence);
        histRepository.delete(history);

    }

    @Transactional
    @Override
    public void tempAbsenceUpdate(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findByMberNoAndStatus(student, ResultStatus.STANDBY);
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);

        history.setChangeDate(new Date());
        history.setResultStatus(ResultStatus.COMPANION);
        histRepository.save(history);

        tempAbsence.setStatus(ResultStatus.COMPANION);

        tempAbsenceRepository.save(tempAbsence);

    }

}
