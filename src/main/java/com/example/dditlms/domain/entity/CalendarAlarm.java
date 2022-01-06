package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "SCHDUL_NTCN")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name="SCHDUL_NTCN_SEQ_GEN",
        sequenceName = "SCHDUL_NTCN_SEQ",
        initialValue =1
)
public class CalendarAlarm {

    @Id
    @Column(name = "SCHDUL_NTCN_SN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "SCHDUL_NTCN_SEQ_GEN")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHDUL_SN")
    private Calendar calendar;

    @Column(name = "SCHDUL_TIME")
    private String scheduleAlarmTime;

    @Column(name = "SCHDUL_CONTENT", length = 200)
    private String scheduleContent;

    @Column(name = "SCHDUL_TYPE", length = 200)
    private String scheduleAlarmType;

    }