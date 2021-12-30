package com.example.dditlms.service.impl;

import com.example.dditlms.controller.MemberForm;
import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.dto.MemberDTO;
import com.example.dditlms.domain.entity.Major;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Student;
import com.example.dditlms.domain.repository.MajorRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.security.AccountContext;
import com.example.dditlms.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MajorRepository majorRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        long userNumber;
        Optional<Member> memberEntityWrapper;
        System.out.println("안되나..?");
        try{
            userNumber = Long.parseLong(account);
            memberEntityWrapper = memberRepository.findByUserNumber(userNumber);
        }catch (NumberFormatException e){
            memberEntityWrapper = memberRepository.findByMemberId(account);
        }
        Member memberEntity = memberEntityWrapper.orElse(null);
        if(memberEntity == null){
            return null;
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getValue()));

        AccountContext accountContext = new AccountContext(memberEntity,authorities);

        return accountContext;
    }

    @Override
    @Transactional
    public String checkUser(MemberForm memberForm,
                            PasswordEncoder passwordEncoder) {
        Optional<Member> memberEntityWrapper =  memberRepository.findByUserNumberAndName(memberForm.getUserNumber(),memberForm.getName());
        Member memberEntity1 = memberEntityWrapper.orElse(null);
        if(memberEntity1 == null){
            return "redirect:/signup?error=true&exception=identification";
        }
        memberEntityWrapper = memberRepository.findByMemberId(memberForm.getMemberId());
        Member memberEntity2 = memberEntityWrapper.orElse(null);
        if(memberEntity2 != null){
            return "redirect:/signup?error=true&exception=overlap";
        }
        memberEntity1.setUserNumber(memberForm.getUserNumber());
        memberEntity1.setMemberId(memberForm.getMemberId());
        memberEntity1.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        memberRepository.save(memberEntity1);
        return "redirect:/login";
    }

    @Override
    @Transactional(readOnly = true)
    public String findId(String identification, String name) {
        long usernumber = Long.parseLong(identification);
        Optional<Member> memberEntityWrapper =  memberRepository.findByUserNumberAndName(usernumber,name);
        Member memberEntity = memberEntityWrapper.orElse(null);
        if(memberEntity == null){
            return null;
        }
        return "id?"+memberEntity.getMemberId();
    }

    @Override
    @Transactional
    public boolean changePW(String id, String password) {
        Optional<Member> memberEntityWrapper = memberRepository.findByMemberId(id);
        Member member = memberEntityWrapper.orElse(null);
        member.setPassword(password);
        member.setFailCount(0);
        memberRepository.save(member);
        return true;
    }

    @Override
    @Transactional
    public void changeData(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void addMembers(List<MemberDTO> saveMembers, Role role) {
        Optional<List<Member>> memberListWrapper = memberRepository.findAllByRoleAndMemberIdIsNull(role);
        List<Member> memberList = memberListWrapper.orElse(null);
        Date now = new Date();
        for(Member member : memberList){
            memberRepository.delete(member);
        }
        for(MemberDTO memberDTO : saveMembers){
            Member member = memberDTO.toEntity();
            memberRepository.save(member);
            Optional<Major> majorWrapper = majorRepository.findById(memberDTO.getMajor());
            Major major = majorWrapper.orElse(null);
            Student student = Student.builder()
                    .userNumber(member.getUserNumber())
                    .major(major)
                    .grade(Grade.ADMISSION)
                    .member(member)
                    .enterDate(now)
                    .semester(1)
                    .academicStatus(AcademicStatus.ATTENDING).build();
            member.setStudent(student);
            memberRepository.save(member);
        }
    }
}
