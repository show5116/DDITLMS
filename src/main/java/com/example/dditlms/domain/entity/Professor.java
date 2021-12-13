package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "PROFSR")
@NoArgsConstructor
@Getter
@ToString
public class Professor {

    @Id
    private Long userNumber;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MBER_NO")
    private Member member;

    @ManyToOne
    @JoinColumn(name="MAJOR_CODE")
    private Major major;


}
