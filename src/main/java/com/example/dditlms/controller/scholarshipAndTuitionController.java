package com.example.dditlms.controller;

import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.RegistrationRepository;
import com.example.dditlms.domain.repository.ScholarshipKindRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.service.SanctnService;
import com.example.dditlms.service.ScholarshipService;
import com.example.dditlms.util.FileUtil;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class scholarshipAndTuitionController {
    private final ScholarshipKindRepository scholarshipKindRepository;

    private final ScholarshipRepository scholarshipRepository;

    private final ScholarshipService scholarshipService;

    private final SemesterByYearRepository semesterByYearRepository;

    private final RegistrationRepository registrationRepository;

    private final SanctnService sanctnService;

    private final FileUtil fileUtil;

    @GetMapping("/student/tuitionShow")
    public ModelAndView tuitionShow(ModelAndView mav){
        Student student = MemberUtil.getLoginMember().getStudent();
        List<Registration> registrationList = registrationRepository.findAllByStudentOrderByRegistDateDesc(student);
        mav.addObject("registrationList",registrationList);
        mav.setViewName("/pages/tuitionShow");
        return mav;
    }

    @GetMapping("/student/tuitionApplication")
    public ModelAndView tuitionApplication(ModelAndView mav){
        Member member = MemberUtil.getLoginMember();
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndSemesterAndStatusAndMethod(member.getStudent(),semester, ResultStatus.APPROVAL, ScholarshipMethod.REDUCTION);
        long sale = 0;
        for(Scholarship scholarship : scholarshipList){
            sale += scholarship.getPrice();
        }
        long payment = member.getStudent().getMajor().getPayment();
        long realPay = payment - sale;
        Optional<Registration> registrationWrapper = registrationRepository.findByStudentAndAplicationSemester(member.getStudent(),semester);
        Registration registration = registrationWrapper.orElse(null);
        if(registration == null){
            mav.addObject("already","false");
        }else{
            mav.addObject("already","true");
        }
        mav.addObject("payment",payment);
        mav.addObject("sale",sale);
        mav.addObject("realPay",realPay);
        mav.addObject("semester",semester);
        mav.addObject("scholarshipList",scholarshipList);
        mav.setViewName("/pages/tuitionApplication");

        //진행현황 결과 출력
        List<SanctnDTO> sanctnDTOS = sanctnService.showScholarshipApply(member.getUserNumber());
        mav.addObject("progress",sanctnDTOS);
        return mav;
    }

    @GetMapping("/student/scholarshipShow")
    public ModelAndView scholarshipShow(ModelAndView mav){
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndStatusNot(MemberUtil.getLoginMember().getStudent(), ResultStatus.STANDBY);
        long sum = 0;
        for(Scholarship scholarship : scholarshipList){
            if(scholarship.getStatus().equals(ResultStatus.APPROVAL)){
                sum += scholarship.getPrice();
            }
        }
        mav.addObject("sum",sum);
        mav.addObject("scholarshipList",scholarshipList);
        mav.setViewName("/pages/scholarshipShow");
        return mav;
    }

    @GetMapping("/student/scholarshipApplication")
    public ModelAndView scholarshipApplication(ModelAndView mav){
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndStatus(MemberUtil.getLoginMember().getStudent(), ResultStatus.STANDBY);
        List<ScholarshipKind> scholarshipKindList = scholarshipKindRepository.findAll();
        mav.addObject("scholarshipList",scholarshipList);
        mav.addObject("scholarshipKindList",scholarshipKindList);
        mav.setViewName("/pages/scholarshipApplication");
        return mav;
    }

    @ResponseBody
    @PostMapping("/student/scholarshipApplication/select")
    public String scholarshipSelect(HttpServletResponse response,
                                    @RequestParam String id){
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        JSONObject jsonObject = new JSONObject();
        Optional<ScholarshipKind> scholarshipKindWrapper = scholarshipKindRepository.findById(id);
        ScholarshipKind scholarshipKind = scholarshipKindWrapper.orElse(null);
        jsonObject.put("criteria",scholarshipKind.getSelectionCriteria());
        return jsonObject.toJSONString();
    }

    @PostMapping("/student/scholarshipApplication/aplication")
    public ModelAndView scholarshipAdd(@RequestParam(value = "file", required = false) MultipartFile file,
                                 @RequestParam String kindId,
                                 MultipartHttpServletRequest request, ModelAndView mav){
        JSONObject jsonObject = new JSONObject();
        long id = fileUtil.uploadFiles(request.getFileMap());
        scholarshipService.addScholarship(id,kindId);
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndStatus(MemberUtil.getLoginMember().getStudent(), ResultStatus.STANDBY);
        mav.addObject("scholarshipList",scholarshipList);
        mav.setViewName("/pages/scholarshipApplication :: #scholarshipList");
        return mav;
    }

}
