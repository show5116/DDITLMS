package com.example.dditlms.controller;

import com.example.dditlms.domain.common.WheatherToDelete;
import com.example.dditlms.domain.dto.SubjectDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.Subject;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SubjectRepository;
import com.example.dditlms.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccademicDepController {

    private final SubjectRepository subjectRepository;

    private final SubjectService subjectService;

    private final MajorRepository majorRepository;

    @GetMapping("/accademic/curriculum")
    public ModelAndView addCurriculum(ModelAndView mav){
        List<Major> majorList = majorRepository.findAll();
        mav.addObject("majorList",majorList);
        mav.setViewName("/pages/curriculum");
        return mav;
    }

    @GetMapping("/accademic/subjectManage")
    public ModelAndView subjectManage(ModelAndView mav){
        List<Subject> subjectList = subjectRepository.findAllByStatus(WheatherToDelete.EXISTED);
        mav.addObject("subjectList",subjectList);
        mav.setViewName("/pages/subjectManage");
        return mav;
    }

    @ResponseBody
    @PostMapping("/accademic/subjectManage/insert")
    public String subjectInsert(@RequestParam SubjectDTO subjectDTO){
        JSONObject jsonObject = new JSONObject();
        subjectService.insertSubject(subjectDTO);
        jsonObject.put("success","ok");
        return jsonObject.toJSONString();
    }

    @ResponseBody
    @PostMapping("/accademic/subjectManage/update")
    public String subjectUpdate(@ModelAttribute SubjectDTO subjectDTO){
        JSONObject jsonObject = new JSONObject();
        subjectService.updateSubject(subjectDTO);
        jsonObject.put("success","ok");
        return jsonObject.toJSONString();
    }
}
