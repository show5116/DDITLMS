package com.example.dditlms.service.impl;

import com.example.dditlms.controller.FileDataController;
import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import com.example.dditlms.domain.entity.TempAbsence;
import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.TempAbsenceRepository;
import com.example.dditlms.domain.repository.sanctn.DocformRepository;
import com.example.dditlms.service.AcademicService;
import com.example.dditlms.service.SanctnService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AcademicServiceImpl implements AcademicService {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);
    private final HistoryRepository histRepository;
    private final TempAbsenceRepository tempAbsenceRepository;
    private final SemesterByYearRepository semesterByYearRepository;
    private final DocformRepository docformRepository;
    private final SanctnService sanctnService;




    @Transactional
    @Override
    public void applicationStatus(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        if(history != null){
            Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findById(history.getTempAbsence().getId());
            TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);
            map.put("tempAbsence", tempAbsence);
        }

        map.put("student", student);
        map.put("history", history);

    }

    @Transactional
    @Override
    public void historyInsert(Map<String, Object> map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Student student = MemberUtil.getLoginMember().getStudent();

        String reason = request.getParameter("reason");
        semesterInsert(map);
        TempAbsence tempAbsence = (TempAbsence) map.get("tempAbsence");

        History history = History.builder()
                .aplicationDate(new Date())
                .status(AcademicStatus.TAKEABREAK)
                .resultStatus(ResultStatus.STANDBY)
                .note(reason)
                .student(student)
                .grade(student.getGrade())
                .major(student.getMajor())
                .tempAbsence(tempAbsence)
                .build();
        logger.info("?????? ?????? : " + student.getGrade());
        histRepository.save(history);


        // ??????????????? ?????? ?????? ??????
        Long drafter = MemberUtil.getLoginMember().getUserNumber();
        Docform docform = docformRepository.findById(6L).get();
        // ????????????, ????????????, ???????????? ?????? ????????? ?????????, ???????????? ?????? ?????? ???????????? ?????????
        // ?????? ?????? ?????? -> ????????? ??????????????? ????????????. ?????? ????????? ?????? ?????? -> ?????? ????????? ????????????
        // ????????? ??????????????? ????????????. ?????? ????????? ?????? ?????? -> ?????? ????????? ?????????
        List<Long> userNumber = new ArrayList<>();
        Long staff = 11134L;
        Long professor = 8888L;
        Long approver = 11139L;
        userNumber.add(staff);
        userNumber.add(professor);
        userNumber.add(approver);
        Long complimentId = history.getId();
        String complimentType = "???????????? : ";
        Long fileId = null;
        sanctnService.saveComplaint(docform, drafter, reason, userNumber,complimentId, complimentType, fileId);
    }

    @Transactional
    @Override
    public void semesterInsert(Map<String, Object> map){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String startDate = request.getParameter("start-date");
        String endDate = request.getParameter("start-date");
        String term = request.getParameter("leave-term");
        int yy = Integer.parseInt(endDate.split("-")[0]);

        if(term.equals("1")){
            if(endDate.substring(endDate.length()-1).equals("1")){
                endDate = yy + "-2";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                endDate = (yy+1) + "-1";
            }
        } else if(term.equals("2")) {
            if(endDate.substring(endDate.length()-1).equals("1")){
                endDate = (yy+1) + "-1";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                endDate = (yy+1) + "-2";
            }
        } else if(term.equals("3")){
            if(endDate.substring(endDate.length()-1).equals("1")){
                endDate = (yy+1) + "-2";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                endDate = (yy+2) + "-1";
            }
        } else if(term.equals("4")){
            if(endDate.substring(endDate.length()-1).equals("1")){
                endDate = (yy+2) + "-1";

            } else if(endDate.substring(endDate.length()-1).equals("2")){
                endDate = (yy+2) + "-2";
            }
        }

        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.findById(startDate);
        SemesterByYear semesterByYear = semesterWrapper.orElse(null);

        Optional<SemesterByYear> semesterEndWrapper = semesterByYearRepository.findById(endDate);
        SemesterByYear semesterByYearEnd = semesterEndWrapper.orElse(null);


        // ????????? ?????? ?????????????????? ???????????????'
        TempAbsence tempAbsence = TempAbsence.builder()
                .beginsemstr(semesterByYear)
                .endsemstr(semesterByYearEnd)
                .status(ResultStatus.STANDBY)
                .build();
        tempAbsenceRepository.save(tempAbsence);

        map.put("tempAbsence", tempAbsence);

    }


    @Transactional
    @Override
    public void cancelService(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        if(map.get("hist").equals("tempAbsence")){

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findById(history.getTempAbsence().getId());
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);
        tempAbsenceRepository.delete(tempAbsence);
        histRepository.delete(history);
        sanctnService.deleteComplaint(history.getId());

        } else if(map.get("hist").equals("change")) {
            Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.CHANGEMAJOR, ResultStatus.STANDBY);
            History history = historyWrapper.orElse(null);
            histRepository.delete(history);

        }


    }

    @Transactional
    @Override
    public void tempAbsenceUpdate(Map<String, Object> map){

        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findById(history.getTempAbsence().getId());
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);

        history.setChangeDate(new Date());
        history.setResultStatus(ResultStatus.COMPANION);
        histRepository.save(history);

        tempAbsence.setStatus(ResultStatus.COMPANION);

        tempAbsenceRepository.save(tempAbsence);

    }


    @Transactional
    @Override
    public void majorChangeHist(Map<String, Object> map){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Student student = MemberUtil.getLoginMember().getStudent();

        String reason = request.getParameter("reason");
        String changeTerm = request.getParameter("change-term");
        logger.info("reson : " + reason);

        History history = History.builder()
                .aplicationDate(new Date())
                .status(AcademicStatus.CHANGEMAJOR)
                .resultStatus(ResultStatus.STANDBY)
                .note(reason)
                .student(student)
                .grade(student.getGrade())
                .major(student.getMajor())
                .build();


    }

}
