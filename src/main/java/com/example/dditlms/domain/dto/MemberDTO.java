package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.MemberDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberDTO {

    private Long userNumber;
    private Role role;
    private String name;
    private String email;
    private String phone;
    private String memberId;
    private String password;
    private String major;


    public Member toEntity(){
        Member member = Member.builder()
                .role(this.role)
                .userNumber(this.userNumber)
                .email(this.email)
                .phone(this.phone)
                .name(this.name).build();
        return member;
    }
}
