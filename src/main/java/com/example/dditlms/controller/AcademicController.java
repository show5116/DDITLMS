package com.example.dditlms.controller;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.dto.EnrolmentDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.EnrolmentRepository;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.ScoreRepository;
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
import java.text.SimpleDateFormat;
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
    private final ScoreRepository scoreRepository;

    @GetMapping("/academic")
    public ModelAndView academic(ModelAndView mav){
        Optional<Student> studentWrapper = studentRepository.findById(MemberUtil.getLoginMember().getUserNumber());
        Student student = studentWrapper.orElse(null);

        List<History> historyList = histRepository.getfindAllByStudent(student);
        List<Enrolment> enrolments = enrolmentRepository.findAllByStudent(student);
        List<EnrolmentDTO> dtoList = new ArrayList<>();
        StringBuilder st = new StringBuilder();

        for(Enrolment enrolment : enrolments){
            enrolment.getOpenLecture().getYearSeme().getYear();

            Optional<List<Score>> scoreListWrapper = scoreRepository.findAllByEnrolment_OpenLecture(enrolment.getOpenLecture());
            List<Score> scoreList = scoreListWrapper.orElse(null);

            List<Double> avgList = new ArrayList<>();
            for(Score score : scoreList){
                double rst = 0;
                if(score.getAttendance()!=0){
                    rst = score.getAttendance()*0.15
                        + score.getTaskScore()*0.25
                        + score.getMiddleScore()*0.3
                        + score.getFinalScore()*0.3;
                } else if(score.getAttendance()==0){
                    rst = 0;
                }
                avgList.add(rst);
            }
            Collections.sort(avgList);

            EnrolmentDTO enrolmentDTO = enrolment.toDTO();
            Optional<Score> scoreWrapper = scoreRepository.findByEnrolment_StudentAndEnrolment_OpenLecture(enrolment.getStudent(),enrolment.getOpenLecture());
            Score myScore = scoreWrapper.orElse(null);
            double myRst = 0;
            if(myScore.getAttendance()!=0){
                myRst = myScore.getAttendance()*0.15
                        + myScore.getTaskScore()*0.25
                        + myScore.getMiddleScore()*0.3
                        + myScore.getFinalScore()*0.3;
            } else if(myScore.getAttendance()==0){
                myRst = 0;
            }

            List<Double> zeroList = new ArrayList<>();
            zeroList.add(0.0);
            avgList.removeAll(zeroList);

            Date date = new Date();
            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            int yy = Integer.parseInt(year.format(date));
            int mon = Integer.parseInt(month.format(date));
            st.append(yy);
            st.append("-");

            if(mon>=1&&mon<=6){
                st.append(1);
            } else {
                st.append(2);
            }

            if(avgList.indexOf(myRst)!=-1){
                int myAvgPer = avgList.indexOf(myRst)/avgList.size()*100;

                String myAvg = null;
                if(myRst==0){
                    myAvg = "F";
                } else if(myAvgPer<=10){
                    myAvg = "A+";
                } else if(myAvgPer<=20){
                    myAvg = "A";
                }  else if(myAvgPer<=30){
                    myAvg = "B+";
                }  else if(myAvgPer<=40){
                    myAvg = "B";
                }  else if(myAvgPer<=60){
                    myAvg = "C+";
                }  else if(myAvgPer<=80){
                    myAvg = "C";
                }  else if(myAvgPer<=90){
                    myAvg = "D+";
                }  else if(myAvgPer<=100){
                    myAvg = "D";
                }
                enrolmentDTO.setAvgScore(myAvg);
                dtoList.add(enrolmentDTO);
            }
        }
        mav.addObject("yearseme", st.toString());
        mav.addObject("student", student);
        mav.addObject("historyList", historyList);
        mav.addObject("enrolments", dtoList);
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

}
