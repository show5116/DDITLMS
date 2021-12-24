package com.example.dditlms.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "YEAR_SEME")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SemesterByYear { //연도별학기 테이블

    @Id
    @Column(name = "YEAR_SEME")
    private String year;

    @Column(name = "YEAR_SEME_START")
    private Date semeStart;

    @Column(name = "YEAR_SEME_END")
    private Date semeEnd;

}
