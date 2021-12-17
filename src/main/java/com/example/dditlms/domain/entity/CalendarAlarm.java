package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.entity.Calendar;
import com.example.dditlms.domain.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "SCHDUL_NTCN")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHDUL_SN")
    private Calendar calendar;

    @Column(name = "SCHDUL_TIME")
    private String scheduleAlarmTime;

    @Column(name = "SCHDUL_CONTENT", length = 200)
    private String scheduleContent;

    @Column(name = "SCHDUL_TYPE", length = 200)
    private String scheduleAlarmType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBER_NO")
    private Member member;

}