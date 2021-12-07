package com.example.dditlms.domain.entity.sanction;

import com.example.dditlms.domain.entity.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "SANCTN_LN_INDV")
@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class SanctnLnIndv {
    @Id
    @Column(name = "SANCTN_LN_SN", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sanctnLnIndvId;

    @Column(name = "SANCTNER", length = 200)
    private String sanctner;

    @Column(name = "SANCTN_SN")
    private Long sanctnSn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Employee mberNo;


}