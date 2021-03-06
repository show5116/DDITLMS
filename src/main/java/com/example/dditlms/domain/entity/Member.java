package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Role;
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
public class Member {

    @Id
    @Column(name="MBER_NO",updatable = false,nullable = false)
    private Long userNumber;

    @Enumerated(EnumType.STRING)
    @Column(name="MBER_SE",nullable = false)
    private Role role;

    @Column(name="MBER_NM",nullable = false)
    private String name;

    @Column(name="EMAIL",nullable = false)
    private String email;

    @Column(name="TELNO",nullable = false)
    private String phone;

    @Column(name="MEMBER_ID")
    private String memberId;

    private String password;

    @Lob
    @Column(name="MEMBER_IMG")
    private String memberImg;

    @Column(name ="FAIL_COUNT")
    @ColumnDefault("0")
    private Integer failCount;

    @Column(name ="MENU_OD")
    @ColumnDefault("'WID101 WID102'")
    private String order;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MemberDetail memberDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Student student;

    @Builder
    public Member(Long userNumber, Role role, String name, String email, String phone) {
        this.userNumber = userNumber;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
