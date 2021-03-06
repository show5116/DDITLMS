package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.dto.DocFormDTO;
import com.example.dditlms.domain.dto.EmployeeDTO;
import com.example.dditlms.domain.dto.PageDTO;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.Attachment;
import com.example.dditlms.domain.entity.Department;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.*;
import com.example.dditlms.domain.repository.AttachmentRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.domain.repository.sanctn.*;
import com.example.dditlms.service.DocformService;
import com.example.dditlms.service.SanctnLnService;
import com.example.dditlms.service.SanctnService;
import com.example.dditlms.util.FileUtil;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SanctnController {

    private final SanctnLnRepository sanctnLnRepository;

    private final MemberRepository memberRepository;

    private final EmployeeRepository employeeRepository;

    private final DocformRepository docformRepository;

    private final DocformService docformService;

    private final DepartmentRepository departmentRepository;

    private final SanctnService sanctnService;

    private final SanctnRepository sanctnRepository;

    private final SanctnLnService sanctnLnService;

    private final FileUtil fileUtil;

    private final AttachmentRepository attachmentRepository;

    //????????????????????? ?????? ???, ???????????? ?????????(?????? ??????, ?????? ?????? & ??????????????? ??????)
    @GetMapping("/sanctn")
    public String santn(Model model, @PageableDefault(size = 8) Pageable pageable, SanctnProgress sanctnProgress) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress reject = SanctnProgress.REJECT;
        Page<SanctnDTO> rejectResult = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, reject);

        long totalRej = rejectResult.getTotalElements();

        model.addAttribute("totalRej", totalRej);

        SanctnProgress pub = SanctnProgress.PUBLICIZE;
        Page<SanctnDTO> pubResult = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, pub);
        long totalPub = pubResult.getTotalElements();
        model.addAttribute("totalPub", totalPub);

        //?????? ??????????????? ????????? ????????? ????????????.

        SanctnProgress progress = SanctnProgress.PROGRESS;
        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, progress);

        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }


        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));
        long totalPro = results.getTotalElements();
        model.addAttribute("totalPro", totalPro);


        //????????? ??? ?????? ?????? ??????, ?????????
        Optional<Member> findUser = memberRepository.findByUserNumber(userNumber);
        String findname = findUser.get().getName();
        model.addAttribute("findname", findname);

        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/re");


        //?????????????????? ??????
        List<SanctnDTO> recentOpinion = sanctnLnRepository.findRecentOpinion(userNumber);
        model.addAttribute("recentOpinions", recentOpinion);

        return "/pages/sanction";
    }

    //??????????????? ????????? ?????? ?????? ??????
    @GetMapping("/sanctn/re")
    public String sanctnRe(Model model, @PageableDefault(size = 8) Pageable pageable, SanctnProgress sanctnProgress) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress progress = SanctnProgress.PROGRESS;
        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, progress);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }

        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));

        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/re");

        return "/pages/sanction::#test";
    }


    // ???????????? ?????????

    @GetMapping("/sanctn/drafting")
    public String drafting(Model model, SanctnForm sanctnForm) {
        //???????????? uesrNumber ??????
        Member loginMember = MemberUtil.getLoginMember();
        model.addAttribute("drafter",loginMember.getUserNumber());
       
        // ???????????? ?????? ??????
        model.addAttribute("findname", loginMember.getName());

        //???????????? ?????? ?????? ???????????? ??????
        List<EmployeeDTO> dtoList = employeeRepository.viewDetails(loginMember.getUserNumber());
        EmployeeDTO empDetails = dtoList.get(0);
        model.addAttribute("empDetails", empDetails);

        //???????????? ?????? ?????????
        List<DocFormCategory> allDocFormList = docformRepository.allDocFormList();
        model.addAttribute("allDocFormList", allDocFormList);

        //?????? ?????? ?????? ??????
        List<Department> departmentList = departmentRepository.findAll();
        model.addAttribute("departmentList", departmentList);

        return "/pages/drafting";
    }

    //????????? 2??? ???????????? ?????? ??????
    @GetMapping("/drafting/sendFormCate")
    @ResponseBody
    public List<DocFormDTO> sendFormCate(@RequestParam Map<String, Object> param) {

        List<DocFormDTO> result = docformRepository.DocFromCate((String) param.get("cate"));

        return result;
    }

    //????????? ???????????? ??????
    @GetMapping("/drafting/sendDept")
    @ResponseBody
    public List<EmployeeDTO> sendDept(@RequestParam Map<String, Object> param) {

        List<EmployeeDTO> empList = employeeRepository.empList(Long.valueOf((String) param.get("dep")));

        return empList;
    }

    //????????? ??????
    @GetMapping("/drafting/makeForm")
    @ResponseBody
    public String makeForm(@RequestParam Map<Long, Object> param) {

        Docform form = docformRepository.findById(Long.valueOf((String) param.get("form"))).get();
        String formContent = docformService.replaceDocform(form.getDocformCn());

        return formContent;
    }

    //?????? ???????????? ??????
    @GetMapping("/viewDetails")
    @ResponseBody
    public List<EmployeeDTO> viewDetails(@RequestParam Map<String, Object> param) {

        List<EmployeeDTO> viewDetails = employeeRepository.viewDetails(Long.valueOf((String) param.get("userNumber")));

        return viewDetails;
    }

    //????????????
    @PostMapping("/sanctn/submit")
    public RedirectView submitSanctn(SanctnForm sanctnForm, @RequestParam(value = "file", required = false) MultipartFile file, MultipartHttpServletRequest request) {

        Map<String,MultipartFile> map = request.getFileMap();

        long id = fileUtil.uploadFiles(map);
        sanctnService.saveSanctn(
                  sanctnForm.getSanctnSj()
                , sanctnForm.getDocformSn()
                , sanctnForm.getDrafter()
                , sanctnForm.getSanctnCn()
                , sanctnForm.getUserNumber()
                , id);

        return new RedirectView("/sanctn");
    }


    //????????? ????????????
    @GetMapping("/showSanctn/{id}")
    public String sanctnDetail(@PathVariable("id") Long id, Model model) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();
        //???????????? ????????? ????????? ?????? ???
        model.addAttribute("userNumber", userNumber);
        Optional<Sanctn> details = sanctnRepository.findById(id);

        if (details.isPresent()) {
            Sanctn sanctn = details.get();
            model.addAttribute("details", sanctn);
            Long drafter = details.get().getDrafter();
            Member findDrafter = memberRepository.findByUserNumber(drafter).get();
            Long atchmnflId = sanctn.getAtchmnflId();
            List<Attachment> attachList = attachmentRepository.findAllById(atchmnflId);
            List<Map<String,String>> tokenList = new ArrayList<>();
            for(Attachment attach : attachList) {
                Map<String,String> token = new HashMap<>();
                token.put("name",attach.getOriginName()+"."+attach.getExtension());
                token.put("token",fileUtil.makeFileToken(attach.getId(), attach.getOrder()));
                tokenList.add(token);
            }
            model.addAttribute("attFile" , tokenList);

            Role role = findDrafter.getRole();
            if (role == Role.ROLE_STUDENT) {
                String compliment = "????????????";
                model.addAttribute("drafterType", compliment);
            }
        }

        //?????? ?????? ??????
        Optional<SanctnDTO> result = sanctnService.viewComplaint(id);

        if (result.isPresent()) {
            SanctnDTO sanctnDTO = null;
            sanctnDTO = result.get();
            model.addAttribute("compliment", sanctnDTO);
        }

        //?????? ?????? ??????
        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);
        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ????????? ??????

        Optional<SanctnDTO> viewComplaintPro = sanctnService.viewComplaintPro(id);
        if (viewComplaintPro.isPresent()) {
            SanctnDTO sanctnDTO = viewComplaintPro.get();
            model.addAttribute("complimentPro", sanctnDTO);
        }

        String s = sanctnService.showSanctnCountProgress(id);
        String[] split = s.split("!");
        Integer Pro = Integer.valueOf(split[0]);
        Integer Total = Integer.valueOf(split[1]);

        int show = 100 / Total;
        int showPro = show * Pro;


        model.addAttribute("showPro", showPro);

        //?????? ID ?????????
        model.addAttribute("id", id);
        return "/pages/sanctionDetail";
    }

    // ?????? ????????????
    @PostMapping("/sanctn/approval")
    public String apropval(@RequestParam Map<String, Object> param, Model model) {

        //?????? ????????? + ???????????? ??????
        Object opinion = param.get("opinion");
        Long userNumber = Long.valueOf((String) param.get("userNumber"));
        Long id = Long.valueOf((String) param.get("id"));

        sanctnLnService.updateSanctnLn(opinion.toString(), userNumber, id);

        Long userNumber2 = MemberUtil.getLoginMember().getUserNumber();

        //???????????? ????????? ????????? ?????? ???
        model.addAttribute("userNumber", userNumber2);

        Optional<Sanctn> details = sanctnRepository.findById(id);
        Sanctn sanctn = details.get();
        model.addAttribute("details", sanctn);

        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);

        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ID ?????????
        model.addAttribute("id", id);

        return "/pages/sanctionDetail";

    }

    //?????? ????????????
    @PostMapping("/sanctn/reject")
    public String reject(@RequestParam Map<String, Object> param, Model model) {


        Object opinion = param.get("opinion");
        Long userNumber = Long.valueOf((String) param.get("userNumber"));
        Long id = Long.valueOf((String) param.get("id"));

        sanctnLnService.rejectSanctnLn(opinion.toString(), userNumber, id);

        Long userNumber2 = MemberUtil.getLoginMember().getUserNumber();


        //???????????? ????????? ????????? ?????? ???
        model.addAttribute("userNumber", userNumber2);

        Optional<Sanctn> details = sanctnRepository.findById(id);
        Sanctn sanctn = details.get();
        model.addAttribute("details", sanctn);

        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);

        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ID ?????????
        model.addAttribute("id", id);

        return "/pages/sanctionDetail";
    }

    //?????? ????????????
    @PostMapping("/sanctn/rejectCompliment")
    public String rejectCompliment(@RequestParam Map<String, Object> param, Model model) {


        Object opinion = param.get("opinion");
        Long userNumber = Long.valueOf((String) param.get("userNumber"));
        Long id = Long.valueOf((String) param.get("id"));
        Long comId = Long.valueOf((String) param.get("comId"));

        sanctnLnService.rejectComplement(opinion.toString(), userNumber, id, comId);

        Long userNumber2 = MemberUtil.getLoginMember().getUserNumber();


        //???????????? ????????? ????????? ?????? ???
        model.addAttribute("userNumber", userNumber2);

        Optional<Sanctn> details = sanctnRepository.findById(id);
        Sanctn sanctn = details.get();
        model.addAttribute("details", sanctn);

        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);

        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ID ?????????
        model.addAttribute("id", id);

        return "/pages/sanctionDetail";
    }


    //????????????
    @PostMapping("/sanctn/finalApproval")
    public String finalApproval(@RequestParam Map<String, Object> param, Model model) {


        Object opinion = param.get("opinion");
        Long userNumber = Long.valueOf((String) param.get("userNumber"));
        Long id = Long.valueOf((String) param.get("id"));

        sanctnLnService.lastUpdateSanctnLn(opinion.toString(), userNumber, id);

        Long userNumber2 = MemberUtil.getLoginMember().getUserNumber();


        //???????????? ????????? ????????? ?????? ???
        model.addAttribute("userNumber", userNumber2);

        Optional<Sanctn> details = sanctnRepository.findById(id);
        Sanctn sanctn = details.get();
        model.addAttribute("details", sanctn);

        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);

        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ID ?????????
        model.addAttribute("id", id);

        return "/pages/sanctionDetail";
    }

    //?????? ????????????
    @PostMapping("/sanctn/finalCompliment")
    public String finalCompliment(@RequestParam Map<String, Object> param, Model model) {


        Object opinion = param.get("opinion");
        Long userNumber = Long.valueOf((String) param.get("userNumber"));
        Long id = Long.valueOf((String) param.get("id"));
        Long comId = Long.valueOf((String) param.get("comId"));
        
        //???????????? & ?????? ???????????? ??????
        sanctnLnService.lastUpdateComplement(opinion.toString(), userNumber, id, comId);

        //???????????? ????????? ????????? ?????? ???
        Long userNumber2 = MemberUtil.getLoginMember().getUserNumber();
        model.addAttribute("userNumber", userNumber2);

        Optional<Sanctn> details = sanctnRepository.findById(id);
        Sanctn sanctn = details.get();
        model.addAttribute("details", sanctn);

        List<SanctnDTO> sanctnDTOS = sanctnLnRepository.showSanctnLine2(id);
        model.addAttribute("sanctnLnList", sanctnDTOS);

        //?????? ID ?????????
        model.addAttribute("id", id);

        return "/pages/sanctionDetail";
    }


    //?????? ??????
    @GetMapping("/sanctn/progress")
    public String sanctnProgress(Model model, @PageableDefault(size = 8) Pageable pageable) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress progress = SanctnProgress.PROGRESS;

        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, progress);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }


        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));

        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/progress");

        return "/pages/sanction::#test";

    }

    //?????? ??????
    @GetMapping("/sanctn/reject")
    public String sanctnReject(Model model, @PageableDefault(size = 8) Pageable pageable) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress reject = SanctnProgress.REJECT;
        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, reject);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }

        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));


        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/reject");

        return "/pages/sanction::#test";
    }

    //?????? ??????

    @GetMapping("/sanctn/public")
    public String sanctnPublic(Model model, @PageableDefault(size = 8) Pageable pageable) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress publicize = SanctnProgress.PUBLICIZE;
        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, publicize);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }

        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));

        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/public");

        return "/pages/sanction::#test";

    }

    // ?????? ??????

    @GetMapping("/sanctn/all")
    public String sanctnAll(Model model, @PageableDefault(size = 8) Pageable pageable) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        Page<SanctnDTO> results = sanctnLnRepository.inquireAll(userNumber, pageable);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }

        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));

        // ????????? ????????? ?????? ??????
        model.addAttribute("mapping", "sanctn/all");

        return "pages/sanction::#test";

    }

    // ?????? ??????
    @GetMapping("/sanctn/com")
    public String sanctnCom(Model model, @PageableDefault(size = 8) Pageable pageable) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();

        SanctnProgress completion = SanctnProgress.COMPLETION;
        Page<SanctnDTO> results = sanctnLnRepository.inquirePageWithProgress(userNumber, pageable, completion);
        for (SanctnDTO result : results) {
            String s = sanctnService.showSanctnCountProgress(result.getSanctnId());
            String[] split = s.split("!");
            String s1 = split[0];
            String s2 = split[1];
            result.setSanctnStep(Integer.valueOf(s1));
            result.setCountPro(Long.valueOf(s2));
        }

        model.addAttribute("results", results);
        model.addAttribute("page", new PageDTO(results.getTotalElements(), pageable));

        return "/pages/sanction::#test";

    }

}