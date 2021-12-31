package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.service.AcademicService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class AcademicController {

    private static final Logger logger = LoggerFactory.getLogger(FileDataController.class);
    private final HistoryRepository histRepository;
    private final AcademicService academicService;
    private final StudentRepository studentRepository;

    @GetMapping("/academic")
    public ModelAndView academic(ModelAndView mav){
        Optional<Student> studentWrapper = studentRepository.findById(MemberUtil.getLoginMember().getUserNumber());
        Student student = studentWrapper.orElse(null);

        List<History> historyList = histRepository.getfindAllByStudent(student);

        mav.addObject("student", student);
        mav.addObject("historyList", historyList);
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

}
