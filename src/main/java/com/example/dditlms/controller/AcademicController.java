package com.example.dditlms.controller;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.History;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Student;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.security.AccountContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final StudentRepository studentRepository;


    @GetMapping("/academic")
    public ModelAndView academic(ModelAndView mav){

        mav.setViewName("pages/academic");
        return mav;
    }

    @GetMapping("/academic/leave")
    public ModelAndView leave(ModelAndView mav){

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }

        //테이블 조회에서 연혁 상태가 휴학이고 변경년월일이 null이고 학번이 member인애
        // 반려당해도 endDate는 주니까
//        Optional<History> histWrapper = histRepository.findByMemberAndHistStatusLikeAndEndDateIsNull(member, "%휴학%");
//        History hist = histWrapper.orElse(null);
        //학생 상태를 뽑아서 찾아 뷰로 보내주고 뷰에서 if(상태가 휴학상태면 return)
//        logger.info(String.valueOf(hist));

        mav.setViewName("pages/leave");
        return mav;
    }

    @PostMapping("/academic/leave")
    public ModelAndView leavePost(ModelAndView mav, HttpServletRequest request){

        Member member = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        } catch (ClassCastException e) {
        }
        Student student = studentRepository.findById(member.getUserNumber()).get();


        String term = request.getParameter("leave-term");
        String value = request.getParameter("leave");
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

        // 여기선 휴학 신청테이블을 만들어야지'
        // 만들어야지≥;'[';/./"?/

        logger.info("term : " + term);
        logger.info("value : " + value);
        logger.info("reason : " + reason);

        mav.setViewName("pages/leave");
        // 현재 상태 띄워주기(변동이력같은거 긁어와서 화면에 보여줌)
        // 현재 상태가 휴학중이면 휴학신청이 불가하다.
        return mav;
    }


}
