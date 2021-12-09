package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.entity.sanction.EmployeeRole;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userNumber;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMP_SE")
    private EmployeeRole employeeRole;

    @Column(name = "ENCPN", length = 200)
    private String encpn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPT_CODE")
    private Department deptCode;

}