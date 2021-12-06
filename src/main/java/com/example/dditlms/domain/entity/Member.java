package com.example.dditlms.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "MBER")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter @Setter
@ToString
public class Member {

    @Id
    @Column(name="MBER_NO",updatable = false,nullable = false)
    private Long userNumber;

    @Column(name="MBER_SE",nullable = false)
    private String selection;

    @Column(name="MBER_NM",nullable = false)
    private String name;

    @Column(name="EMAIL",nullable = false)
    private String email;

    @Column(name="TELNO",nullable = false)
    private String phone;

    @Column(name="BANK_NM")
    private String bankName;

    @Column(name="ACNUT_NO")
    private String bankAccount;

    @Column(name="MEMBER_ID")
    private String memberId;

    private String password;

    @Column(name="member_img")
    @ColumnDefault("'user'")
    private String memberImg;

    @Column(name ="fail_count")
    @ColumnDefault("0")
    private Integer failCount;

    @Builder
    public Member(Long userNumber, String selection, String name, String email, String phone) {
        this.userNumber = userNumber;
        this.selection = selection;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
