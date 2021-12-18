package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "SCHDUL_NTCN")
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarAlarm {

    @Id
    @Column(name = "SCHDUL_SN")
    @GeneratedValue(strategy = GenerationType.AUTO)
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