package com.example.dditlms.domain.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CALENDAR")
@Getter
public class Calendar {

    @Id
    @Column(name = "CALENDAR_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_NUMBER")
    private Member member;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "SCHEDULE_STR")
    private Date scheduleStr;

    @Column(name = "SCHEDULE_END")
    private Date scheduleEnd;

    @Column(name = "PLACE")
    private String place;



}
