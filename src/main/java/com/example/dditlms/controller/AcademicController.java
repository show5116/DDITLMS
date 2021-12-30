package com.example.dditlms.controller;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.TempAbsenceRepository;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AcademicController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);
    private final HistoryRepository histRepository;
    private final TempAbsenceRepository tempAbsenceRepository;
    private final SemesterByYearRepository semesterByYearRepository;


    @GetMapping("/academic")
    public ModelAndView academic(ModelAndView mav){

        mav.setViewName("pages/academic");
        return mav;
    }

    @GetMapping("/academic/leave")
    public ModelAndView leave(ModelAndView mav){

        Student student = MemberUtil.getLoginMember().getStudent();

        Date date = new Date();
        System.out.println("에라이" + date.getMonth()+1);
        int month = date.getMonth()+1;
        if(month>=1 && month <= 6){
            mav.addObject("mon", "1학기");
            System.out.println("1학기");
        } else if(month>=7 && month <= 12){
            mav.addObject("mon", "2학기");
            System.out.println("2학기");
        } else {
            System.out.println("몰라");
        }

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);
        if(history != null){
        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findByMberNoAndStatus(student, ResultStatus.STANDBY);
            TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);
            mav.addObject("tempAbsence", tempAbsence);
            mav.addObject("student", student);
            mav.addObject("parent", student.getMajor().getParent());

        }

        mav.addObject("history", history);
        mav.setViewName("pages/leave");
        return mav;
    }

    @PostMapping("/academic/leavePost")
    public ModelAndView leavePost(ModelAndView mav, HttpServletRequest request){
        Student student = MemberUtil.getLoginMember().getStudent();

        String startDate = request.getParameter("start-date");
        String endDate = request.getParameter("start-date");
        String term = request.getParameter("leave-term");
        String value = request.getParameter("leave");
        String reason = request.getParameter("reason");
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

        logger.info("startDate : " + startDate);
        logger.info("term : " + term);
        logger.info("value : " + value);
        logger.info("reason : " + reason);

        mav.setViewName("redirect:/academic/leave");
        // 현재 상태 띄워주기(변동이력같은거 긁어와서 화면에 보여줌)
        // 현재 상태가 휴학중이면 휴학신청이 불가하다.
        return mav;
    }

    @PostMapping("/academic/cancel")
    public ModelAndView cancel(ModelAndView mav){
        Student student = MemberUtil.getLoginMember().getStudent();

        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.TAKEABREAK, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Optional<TempAbsence> tempAbsenceWrapper = tempAbsenceRepository.findByMberNoAndStatus(student, ResultStatus.STANDBY);
        TempAbsence tempAbsence = tempAbsenceWrapper.orElse(null);
        tempAbsenceRepository.delete(tempAbsence);
        histRepository.delete(history);

        mav.setViewName("redirect:/academic/leave");

        return mav;
    }


}
