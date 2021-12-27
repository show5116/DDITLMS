package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "SCHDUL")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name="SCHDUL_SEQ_GEN",
        sequenceName = "SCHDUL_SEQ",
        initialValue =1
)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "SCHDUL_SEQ_GEN")
    @Column(name = "SCHDUL_SN", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Member member;

    @Column(name = "SCHDUL_NM")
    private String title;

    @Lob
    @Column(name = "SCHDUL_CN")
    private String content;

    @Column(name = "SCHDUL_PLACE")
    private String scheduleLocation;

    @Column(name = "SCHDUL_BGNDE")
    private String scheduleStr;

    @Column(name = "SCHDUL_ENDDE")
    private String scheduleEnd;

    @Column(name = "SCHDUL_ALARM_TIME")
    private String setAlarmTime;

    @Column(name = "SCHDUL_OTHBC_TRGET", length = 30)
    private String scheduleTypeDetail;

    @Column(name = "SCHDUL_OTHBCSE", length = 30)
    private String scheduleType;

}