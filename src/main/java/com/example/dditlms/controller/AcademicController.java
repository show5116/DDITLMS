package com.example.dditlms.controller;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.EnrolmentRepository;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.service.AcademicService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class AcademicController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);
    private final HistoryRepository histRepository;
    private final AcademicService academicService;
    private final StudentRepository studentRepository;
    private final EnrolmentRepository enrolmentRepository;
    private final MajorRepository majorRepository;

    @GetMapping("/academic")
    public ModelAndView academic(ModelAndView mav){
        Optional<Student> studentWrapper = studentRepository.findById(MemberUtil.getLoginMember().getUserNumber());
        Student student = studentWrapper.orElse(null);

        List<History> historyList = histRepository.getfindAllByStudent(student);
        List<Enrolment> enrolments = enrolmentRepository.findAllByStudent(student);

        mav.addObject("student", student);
        mav.addObject("historyList", historyList);
        mav.addObject("enrolments", enrolments);
        mav.setViewName("pages/academic");
        return mav;
    }

    @GetMapping("/academic/leave")
    public ModelAndView leave(ModelAndView mav){

        Map<String, Object> map = new HashMap<>();
        academicService.applicationStatus(map);

        Student student = (Student) map.get("student");
        History history = (History) map.get("history");

        Date date = new Date();
        int month = date.getMonth()+1;
        if(month>=1 && month <= 6){
            mav.addObject("mon", "1학기");
        } else if(month>=7 && month <= 12){
            mav.addObject("mon", "2학기");
        }

        if(history != null){
            TempAbsence tempAbsence = (TempAbsence) map.get("tempAbsence");
            mav.addObject("student", student);
            mav.addObject("tempAbsence", tempAbsence);
            mav.addObject("parent", student.getMajor().getParent());
        }

        mav.addObject("history", history);
        mav.setViewName("pages/leave");
        return mav;
    }

    @PostMapping("/academic/leavePost")
    public ModelAndView leavePost(ModelAndView mav){
        Map<String, Object> map = new HashMap<>();
        academicService.historyInsert(map);

        mav.setViewName("redirect:/academic/leave");
        return mav;
    }

    @PostMapping("/academic/cancel")
    public ModelAndView cancel(ModelAndView mav){

        Map<String, Object> map = new HashMap<>();
        map.put("hist", "tempAbsence");
        academicService.cancelService(map);

        mav.setViewName("redirect:/academic/leave");

        return mav;
    }

    @GetMapping("/academic/test")
    public String test(){
        Map<String, Object> map = new HashMap<>();
        academicService.tempAbsenceUpdate(map);
        return "pages/leave";
    }

    @GetMapping("/academic/changeMajor")
    public ModelAndView change(ModelAndView mav){
        Student student = MemberUtil.getLoginMember().getStudent();

        List<Major> majorList = majorRepository.findAll();
        Optional<History> historyWrapper = histRepository.findByStudentAndStatusAndResultStatus(student, AcademicStatus.CHANGEMAJOR, ResultStatus.STANDBY);
        History history = historyWrapper.orElse(null);

        Date date = new Date();
        int month = date.getMonth()+1;
        if(month>=1 && month <= 6){
            mav.addObject("mon", "1학기");
        } else if(month>=7 && month <= 12){
            mav.addObject("mon", "2학기");
        }

        mav.addObject("history", history);
        mav.addObject("student", student);
        mav.addObject("majorList", majorList);
        mav.setViewName("pages/changeMajor");
        return mav;
    }

    @PostMapping("/academic/changePost")
    public ModelAndView changePost(ModelAndView mav){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Student student = MemberUtil.getLoginMember().getStudent();

        String reason = request.getParameter("reason");
        String changeTerm = request.getParameter("change-term");

        Optional<Major> majorWrapper = majorRepository.findById(changeTerm);
        Major major = majorWrapper.orElse(null);

        History history = History.builder()
                .aplicationDate(new Date())
                .status(AcademicStatus.CHANGEMAJOR)
                .resultStatus(ResultStatus.STANDBY)
                .note(reason)
                .student(student)
                .grade(student.getGrade())
                .major(major)
                .build();
        histRepository.save(history);


        mav.setViewName("redirect:/academic/changeMajor");

        return mav;
    }

    @PostMapping("/academic/changeCancel")
    public ModelAndView changeCancel(ModelAndView mav){

        Map<String, Object> map = new HashMap<>();
        map.put("hist", "change");
        academicService.cancelService(map);

        mav.setViewName("redirect:/academic/changeMajor");
        return mav;
    }

//    public ModelAndView attendHistory(ModelAndView mav){
//        Student student = MemberUtil.getLoginMember().getStudent();
//
//        Map<String, Object> map = new HashMap<>();
//        List<Enrolment> enrolments = enrolmentRepository.findAllByStudent(student);
//
//        return mav;
//    }

}
