package com.example.dditlms.controller;

import com.example.dditlms.domain.dto.DocFormDTO;
import com.example.dditlms.domain.dto.EmployeeDTO;
import com.example.dditlms.domain.entity.Department;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.*;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.*;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.SanctnService;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SanctnController {

    private final SanctnLnRepository sanctnLnRepository;

    private final MemberRepository memberRepository;

    private final EmployeeRepository employeeRepository;

    private final DocformRepository docformRepository;

    private final DepartmentRepository departmentRepository;

    private final SanctnService sanctnService;

    private final SanctnRepository sanctnRepository;

    //결재메인페이지 접속 시, 기본정보 출력용(단순 조회, 전체 숫자 & 진행정보만 출력)
    @GetMapping("/sanctn")
    public String santn(Model model) {
        
        //현재 로그인한 사용자 정보(userNumber)를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        }catch (ClassCastException e){
        }
        Long userNumber = member.getUserNumber();

//        //진행중인 결재 내용 조회
//        QueryResults<SanctnLn> resultsPro = sanctnLnRepository.inquireProgress(userNumber);
//        long totalPro = resultsPro.getTotal();
//        QueryResults<SanctnLn> resultsRej = sanctnLnRepository.inquireReject(userNumber);
//        long totalRej = resultsRej.getTotal();
//        QueryResults<SanctnLn> resultsPub = sanctnLnRepository.inquirePublicize(userNumber);
//        long totalPub = resultsPub.getTotal();
//        QueryResults<SanctnLn> resultsCom = sanctnLnRepository.inquireCompletion(userNumber);
//        long totalCom = resultsCom.getTotal();

//        List<SanctnLn> proDetails = resultsPro.getResults();
//        model.addAttribute("totalPro", totalPro);
//        model.addAttribute("proDetails", proDetails);
//        model.addAttribute("totalRej", totalRej);
//        model.addAttribute("totalPub", totalPub);
//        model.addAttribute("totalCom", totalCom);
        
        //로그인 한 사람 이름 조회, 넘기기
        Optional<Member> findUser = memberRepository.findByUserNumber(userNumber);
        String findname = findUser.get().getName();
        model.addAttribute("findname", findname);


        return "/pages/sanction";
    }

    @GetMapping("/drafting")
    public String drafting(Model model, SanctnForm sanctnForm) {
        //로그인한 사람 정보 조회 및 데이터 넘김
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;
        try {
            member = ((AccountContext) authentication.getPrincipal()).getMember();
        }catch (ClassCastException e){
        }
        Long userNumber = member.getUserNumber();
        model.addAttribute("drafter", userNumber);

        // 로그인한 사람 이름 얻기
        Optional<Member> findUser = memberRepository.findByUserNumber(userNumber);
        String findname = findUser.get().getName();
        model.addAttribute("findname", findname);
        
        //로그인한 사람 직원 상세정보 조회
        List<EmployeeDTO> dtoList = employeeRepository.viewDetails(userNumber);
        EmployeeDTO empDetails = dtoList.get(0);
        model.addAttribute("empDetails", empDetails);

        
        //문서양식 전부 가져옴
        List<DocFormCategory> allDocFormList = docformRepository.allDocFormList();
        model.addAttribute("allDocFormList", allDocFormList);
        
        //부서 목록 전체 조회
        List<Department> departmentList = departmentRepository.findAll();
        model.addAttribute("departmentList",departmentList);
        log.info(String.valueOf(departmentList));
        

        return "/pages/drafting";
    }

    //양식폼 2차 카테고리 결과 반환
    @GetMapping("/sendFormCate")
    @ResponseBody
    public List<DocFormDTO> sendFormCate(@RequestParam Map<String, Object> param) {

        List<DocFormDTO> result = docformRepository.DocFromCate((String) param.get("cate"));

        return result;
    }
    //부서별 직원목록 반환
    @GetMapping("/sendDept")
    @ResponseBody
    public List<EmployeeDTO> sendDept(@RequestParam Map<String, Object> param) {

        List<EmployeeDTO> empList = employeeRepository.empList(Long.valueOf((String) param.get("dep")));

        return empList;
    }

    //양식폼 생성
    @GetMapping("/makeForm")
    @ResponseBody
    public Optional<Docform> makeForm(@RequestParam Map<Long, Object> param) {

        Optional<Docform> form = docformRepository.findById(Long.valueOf((String) param.get("form")));


        return form;
    }

    //직원 상세정보 조회
    @GetMapping("/viewDetails")
    @ResponseBody
    public List<EmployeeDTO> viewDetails(@RequestParam Map<String, Object> param) {
        log.info((String) param.get("userNumber"));

        List<EmployeeDTO> viewDetails = employeeRepository.viewDetails(Long.valueOf((String)param.get("userNumber")));
        log.info(String.valueOf(viewDetails));

        return viewDetails;
    }
    //기안하기
    @PostMapping("/sanctnSubmit")
    public Docform submitSanctn(SanctnForm sanctnForm) {


        sanctnService.saveSanctn(sanctnForm.getSanctnSj()
                ,sanctnForm.getDocformSn()
                ,sanctnForm.getDrafter()
                ,sanctnForm.getSanctnCn());


        return null;
    }



}
