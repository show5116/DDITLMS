package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "IDENTIFICATION")
@NoArgsConstructor
@Getter @Setter
@ToString
public class Identification {

    @Id
    @Column(name="user_number")
    private long userNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String d_type;

    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;

    @OneToOne(mappedBy = "identification",cascade = CascadeType.ALL)
    private Member member;

    @Builder
    public Identification(long userNumber,String name,String email,String d_type,String phoneNumber,
                          Member member){
        this.userNumber = userNumber;
        this.name = name;
        this.d_type = d_type;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.member = member;
    }

}
