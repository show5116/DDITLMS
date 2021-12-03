package com.example.dditlms.service;

import com.example.dditlms.controller.MemberForm;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface MemberService extends UserDetailsService {
    public String checkUser(MemberForm memberForm, PasswordEncoder passwordEncoder);
    public String findId(String identification,String name);
    public boolean changePW(String id,String password);
}
