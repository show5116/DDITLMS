package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.CalendarAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarAlarmRepository extends JpaRepository<CalendarAlarm, Long>, CalendarAlarmRepositoryCustom{
}
