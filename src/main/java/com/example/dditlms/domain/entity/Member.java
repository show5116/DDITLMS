package com.example.dditlms.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter @Setter
@ToString
public class Member {

    @MapsId
    @OneToOne(cascade =CascadeType.ALL)
    @JoinColumn(name="user_number")
    private Identification identification;

    @Id
    @Column(name="user_number",updatable = false,nullable = false)
    private Long userNumber;

    @Column(name="member_id",nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(name="member_img")
    @ColumnDefault("'user'")
    private String memberImg;

    @Column(name ="fail_count")
    @ColumnDefault("0")
    private Integer failCount;

    @Builder
    public Member(Identification identification,long userNumber, String memberId, String password){
        this.identification = identification;
        this.userNumber = userNumber;
        this.memberId = memberId;
        this.password = password;
    }

}
