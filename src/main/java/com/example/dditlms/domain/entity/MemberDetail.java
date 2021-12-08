package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "MBER_DETAIL")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDetail {
    @Id
    private Long userNumber;

    @MapsId
    @OneToOne
    @JoinColumn(name = "MBER_NO", nullable = false)
    private Member member;

    @Column(name = "BANK")
    private String bank;

    @Column(name = "ACNUTNO")
    private String acnutNo;

    @Column(name = "ZIP")
    private String zip;

    @Column(name = "RDNM_ADR")
    private String rdnmAdr;

    @Column(name = "LNM_ADR")
    private String lnmAdr;

    @Column(name = "DETAIL_ADR")
    private String detailAdr;

    @Column(name = "REFER")
    private String refer;

    @Column(name = "SELF_INTR")
    private String selfIntr;

}