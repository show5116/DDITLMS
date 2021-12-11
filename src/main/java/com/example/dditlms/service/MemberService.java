package com.example.dditlms.service;

import com.example.dditlms.controller.MemberForm;
import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.dto.MemberDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.MemberDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberService extends UserDetailsService {
    public String checkUser(MemberForm memberForm, PasswordEncoder passwordEncoder);
    public String findId(String identification,String name);
    public boolean changePW(String id,String password);
    public void changeData(Member member);
    public void addMembers(List<MemberDTO> saveMembers, Role role);
}
