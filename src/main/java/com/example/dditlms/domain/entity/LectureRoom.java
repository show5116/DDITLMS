package com.example.dditlms.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LCTRUM")
public class LectureRoom {

    @Id
    @Column(name = "LCTRUM_CD", nullable = false)
    private String id;

    @Column(name = "LCTRUM_BLDG")
    private String roomNumber;

    @Column(name = "LCTRUM_MXMM_NMPR")
    private Long maxNumberPeople;

}