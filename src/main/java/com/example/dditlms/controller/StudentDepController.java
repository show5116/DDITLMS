package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.StudentDTO;
import com.example.dditlms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentDepController {

    private final StudentService studentService;

    @GetMapping("/studentDep/register")
    public ModelAndView registerAdmin(ModelAndView mav){
        List<StudentDTO> studentList = studentService.getNotRegistedStudents();
        mav.addObject("studentList",studentList);
        mav.setViewName("/pages/registerAdmin");
        return mav;
    }

    @PostMapping("/studentDep/register/completePay")
    public ModelAndView completePay(ModelAndView mav,
                                    @RequestParam Long id){
        studentService.payTuition(id);
        List<StudentDTO> studentList = studentService.getNotRegistedStudents();
        mav.addObject("studentList",studentList);
        mav.setViewName("/pages/registerAdmin :: #not-regist-students");
        return mav;
    }

    @PostMapping("/studentDep/register/notPay")
    public ModelAndView notPay(ModelAndView mav,
                               @RequestParam Long id){
        studentService.notPayTuition(id);
        List<StudentDTO> studentList = studentService.getNotRegistedStudents();
        mav.addObject("studentList",studentList);
        mav.setViewName("/pages/registerAdmin :: #not-regist-students");
        return mav;
    }

    @GetMapping("/studentDep/studentAssign")
    public ModelAndView studentAssign(ModelAndView mav){
        return mav;
    }
}
