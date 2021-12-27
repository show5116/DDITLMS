package com.example.dditlms.domain.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@Table(name="NTCN")
@Getter @Setter
@Where(clause = "DELETE_AT = 'N'")
@SequenceGenerator(
        name="NTCN_SEQ_GEN",
        sequenceName = "NTCN_SEQ",
        initialValue =1
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "NTCN_SEQ_GEN")
    @Column(name="NTCN_SN")
    private Long id;

    @Column(name="OCCRRNC_TIME",nullable = false)
    private Date enterDate;

    @Column(name="NTCN_NM",nullable = false)
    private String name;

    @Column(name="NTCN_CT",nullable = false)
    private String content;

    @Column(nullable = false)
    private String URL;

    @Column(name="DELETE_AT",nullable = false)
    @ColumnDefault("'N'")
    private Character delete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MBER_NO",nullable = false)
    private Member member;
}
