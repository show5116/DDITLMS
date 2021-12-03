package com.example.dditlms.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CALENDAR_CLASSIFY")
public class CalendarClassify implements Serializable {

    @Id
    @ManyToOne
    private Calendar calendar;

    @Column(name = "CALENDAR_CLASSIFY")
    private String classify;

    @Column(name = "TARGET")
    private String target;


}
