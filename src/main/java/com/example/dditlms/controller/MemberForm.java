package com.example.dditlms.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MemberForm {
    private Long userNumber;
    private String memberId;
    private String password;
    private String name;

}
