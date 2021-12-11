package com.example.dditlms.controller;

import com.example.dditlms.domain.common.Major;
import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.dto.MemberDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AddMemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @GetMapping("/admin/addMember")
    public ModelAndView addMember(ModelAndView mav){
        Optional<List<Member>> studentWrapper = memberRepository.findAllByRoleAndMemberIdIsNull(Role.ROLE_STUDENT);
        List<Member> studentList = studentWrapper.orElse(null);
        mav.addObject("studentList",studentList);
        mav.addObject("majors",Major.values());
        mav.setViewName("/pages/addMember");
        return mav;
    }

    @PostMapping("/admin/addMember/student")
    public void saveMember(HttpServletResponse response, HttpServletRequest request,
                           @RequestBody Map<String,List<MemberDTO>> paramMap){
        JSONObject jsonObject = new JSONObject();
        List<MemberDTO> saveMembers = paramMap.get("saveMember");
        memberService.addMembers(saveMembers,Role.ROLE_STUDENT);
        jsonObject.put("state","true");
        try {
            response.getWriter().print(jsonObject.toJSONString());
        } catch (IOException e) {
        }
    }
}

