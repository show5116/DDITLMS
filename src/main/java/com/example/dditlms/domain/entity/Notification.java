package com.example.dditlms.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name="NTCN")
@Getter
@Where(clause = "delete = 'Y'")
public class Notification {

    @Id
    @GeneratedValue
    @Column(name="NTCN_SN")
    private Long id;

    @Column(name="OCCRRNC_TIME")
    private Date enterDate;

    @Column(name="NTCN_NM")
    private String name;

    @Column
    private String URL;

    @Column(name="DELETE_AT")
    private Character delete;

    @ManyToOne
    @JoinColumn(name="MBER_NO")
    private Member member;
}
