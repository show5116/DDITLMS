package com.example.dditlms.domain.idclass;

import com.example.dditlms.domain.entity.Calendar;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CalendarAlarmId {

    private Long alarmId;

    private Calendar calendar;


}
