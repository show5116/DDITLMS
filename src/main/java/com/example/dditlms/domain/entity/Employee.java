package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "EMP")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    private Long userNumber;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO", nullable = false)
    private Member member;

    @Column(name = "EMP_SE", length = 50)
    private String empSe;

    @Column(name = "ENCPN", length = 200)
    private String encpn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPT_CODE")
    private Department deptCode;

}