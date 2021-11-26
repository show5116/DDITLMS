package com.example.dditlms.controller;

import com.example.dditlms.domain.entity.Identification;
import com.example.dditlms.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MemberForm {
    private Integer userNumber;
    private String memberId;
    private String password;
    private String name;

    public Member ToEntity(){
        Member member = Member.builder().userNumber(userNumber)
                .memberId(memberId)
                .password(password)
                .build();
        return member;
    }
}
