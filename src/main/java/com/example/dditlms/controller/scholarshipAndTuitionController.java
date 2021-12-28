package com.example.dditlms.controller;

import com.example.dditlms.domain.common.ScholarshipStatus;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.ScholarshipKind;
import com.example.dditlms.domain.repository.ScholarshipKindRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class scholarshipAndTuitionController {
    private final ScholarshipKindRepository scholarshipKindRepository;

    private final ScholarshipRepository scholarshipRepository;

    private final ScholarshipService scholarshipService;

    private final FileUtil fileUtil;

    @GetMapping("/student/tuitionShow")
    public String tuitionShow(){
        return "";
    }

    @GetMapping("/student/tuitionApplication")
    public String tuitionApplication(){
        return "";
    }

    @GetMapping("/student/scholarshipShow")
    public ModelAndView scholarshipShow(ModelAndView mav){
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByMemberAndStatusNot(MemberUtil.getLoginMember(), ScholarshipStatus.STANDBY);
        long sum = 0;
        for(Scholarship scholarship : scholarshipList){
            if(scholarship.getStatus().equals(ScholarshipStatus.APPROVAL)){
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
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByMemberAndStatus(MemberUtil.getLoginMember(), ScholarshipStatus.STANDBY);
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
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByMemberAndStatus(MemberUtil.getLoginMember(), ScholarshipStatus.STANDBY);
        mav.addObject("scholarshipList",scholarshipList);
        mav.setViewName("/pages/scholarshipApplication :: #scholarshipList");
        return mav;
    }

}
