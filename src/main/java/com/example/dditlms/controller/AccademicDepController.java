package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.LectureSection;
import com.example.dditlms.domain.common.WheatherToDelete;
import com.example.dditlms.domain.dto.CurriculumDTO;
import com.example.dditlms.domain.dto.SubjectDTO;
import com.example.dditlms.domain.entity.Curriculum;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Subject;
import com.example.dditlms.domain.repository.CurriculumRepository;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.SubjectRepository;
import com.example.dditlms.service.CurriculumService;
import com.example.dditlms.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AccademicDepController {

    private final CurriculumService curriculumService;

    private final SubjectRepository subjectRepository;

    private final SubjectService subjectService;

    private final MajorRepository majorRepository;

    private final SemesterByYearRepository semesterRepository;

    @GetMapping("/accademic/curriculum")
    public ModelAndView addCurriculum(ModelAndView mav){
        SemesterByYear semester = semesterRepository.selectNextSeme().get();
        List<Major> majorList = majorRepository.findAll();
        List<Subject> subjectList = subjectRepository.findAllByStatus(WheatherToDelete.EXISTED);
        List<SemesterByYear> semesterList = semesterRepository.findAllByOrderByYearDesc();
        List<Curriculum> curriculumList = curriculumService.searchCurriculums(majorList.get(0).getId(),semesterList.get(0).getYear(),Grade.FRESHMAN,LectureSection.CULTURE_SELECT);
        mav.addObject("duplicate",false);
        mav.addObject("semester",semester);
        mav.addObject("majorList",majorList);
        mav.addObject("subjectList",subjectList);
        mav.addObject("semesterList",semesterList);
        mav.addObject("curriculumList",curriculumList);
        mav.setViewName("/pages/curriculum");
        return mav;
    }

    @PostMapping("/accademic/curriculum/insert")
    public ModelAndView insertCurriculum(ModelAndView mav, @RequestParam String major, @RequestParam Grade grade,
                                         @RequestParam String semester, @RequestParam LectureSection section,
                                         @RequestParam String subject){
        Boolean duplicate = curriculumService.insertCurriculum(major,semester,grade,section,subject);
        List<Curriculum> curriculumList = curriculumService.searchCurriculums(major,semester,grade,section);
        mav.addObject("duplicate",duplicate);
        mav.addObject("curriculumList",curriculumList);
        mav.setViewName("/pages/curriculum::#curriculum-table");
        return mav;
    }

    @PostMapping("/accademic/curriculum/delete")
    public ModelAndView deleteCurriculum(ModelAndView mav, @RequestParam String major, @RequestParam Grade grade,
                                         @RequestParam String semester, @RequestParam LectureSection section,
                                         @RequestParam Long curriculum){
        List<Curriculum> curriculumList = curriculumService.deleteCurriculum(major,semester,grade,section,curriculum);
        mav.addObject("duplicate",false);
        mav.addObject("curriculumList",curriculumList);
        mav.setViewName("/pages/curriculum::#curriculum-table");
        return mav;
    }

    @PostMapping("/accademic/curriculum/search")
    public ModelAndView searchCurriculum(ModelAndView mav, @RequestParam String major, @RequestParam Grade grade,
                                                @RequestParam String semester, @RequestParam LectureSection section){
        List<Curriculum> curriculumList = curriculumService.searchCurriculums(major,semester,grade,section);
        mav.addObject("curriculumList",curriculumList);
        mav.setViewName("/pages/curriculum::#curriculum-table");
        return mav;
    }

    @PostMapping("/accademic/curriculum/excel")
    public ModelAndView excelCurriculum(ModelAndView mav, @RequestBody Map<String,Object> requestMap){
        String major = requestMap.get("major")+"";
        Grade grade = Grade.valueOf(requestMap.get("grade")+"");
        LectureSection section = LectureSection.valueOf(requestMap.get("section")+"");
        String semester = requestMap.get("semester")+"";
        List<CurriculumDTO> curriculumDTOList = (List<CurriculumDTO>)requestMap.get("curriculumList");
        List<Curriculum> curriculumList = curriculumService.searchCurriculums(major,semester,grade,section);
        mav.addObject("curriculumList",curriculumList);
        mav.setViewName("/pages/curriculum::#curriculum-table");
        return mav;
    }

    @ResponseBody
    @PostMapping("/accademic/curriculum/export")
    public List<CurriculumDTO> excelExport(){
        List<CurriculumDTO> curriculumDTOList = curriculumService.exportCurriculums();
        return  curriculumDTOList;
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
