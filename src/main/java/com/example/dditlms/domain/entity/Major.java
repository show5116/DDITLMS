package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.MajorSelection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name="MAJOR")
@Getter
public class Major{

    @Id
    @Column(name = "MAJOR_CODE")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "MAJOR_SE",nullable = true)
    private MajorSelection selection;

    @Column(name = "MAJOR_NM",nullable = true)
    private String name;

    @Column(name = "MAJOR_NM_KR")
    private String korean;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_MAJOR_CODE")
    private Major parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_MAJOR_CODE")
    private List<Major> children;
}
