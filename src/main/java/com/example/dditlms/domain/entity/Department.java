package com.example.dditlms.domain.entity;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "DEPT")
@Entity
@RequiredArgsConstructor
@ToString
public class Department {
    @Id
    @Column(name = "DEPT_CODE", nullable = false, length = 200)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}