package com.example.dditlms.service.impl;

import com.example.dditlms.controller.MemberForm;
import com.example.dditlms.domain.entity.Identification;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.IdentificationRepository;
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
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final IdentificationRepository identificationRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        long userNumber;
        Optional<Member> memberEntityWrapper;
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
        authorities.add(new SimpleGrantedAuthority(memberEntity.getIdentification().getD_type()));

        AccountContext accountContext = new AccountContext(memberEntity,authorities);

        return accountContext;
    }

    @Override
    @Transactional
    public String checkUser(MemberForm memberForm,
                            PasswordEncoder passwordEncoder) {
        Optional<Identification> identificationWrapper =  identificationRepository.findByUserNumberAndName(memberForm.getUserNumber(),memberForm.getName());
        Identification identification = identificationWrapper.orElse(null);
        if(identification == null){
            return "redirect:/signup?error=true&exception=identification";
        }
        Optional<Member> memberEntityWrapper = memberRepository.findByMemberId(memberForm.getMemberId());
        Member memberEntity = memberEntityWrapper.orElse(null);
        if(memberEntity != null){
            return "redirect:/signup?error=true&exception=overlap";
        }
        Member member = Member.builder()
                .identification(identification)
                .userNumber(identification.getUserNumber())
                .memberId(memberForm.getMemberId())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .build();
        memberRepository.save(member);
        return "redirect:/login";
    }

    @Override
    public String findId(String identification, String name) {
        long usernumber = Long.parseLong(identification);
        Optional<Identification> identificationWrapper =  identificationRepository.findByUserNumberAndName(usernumber,name);
        Identification identificationEntity = identificationWrapper.orElse(null);
        if(identificationEntity == null){
            return null;
        }
        try{
            Member member = identificationEntity.getMember();
            return "id?"+member.getMemberId();
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean changePW(String id, String password) {
        Optional<Member> memberEntityWrapper = memberRepository.findByMemberId(id);
        Member member = memberEntityWrapper.orElse(null);
        member.setPassword(password);
        member.setFailCount(0);
        memberRepository.save(member);
        return true;
    }
}
