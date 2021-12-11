package com.example.dditlms.domain.entity.sanction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class SanctnForm {

    private Long sanctnId;
    private String sanctnSj;
    private String sanctnCn;
    private LocalDate sanctnWritngde;
    private LocalDate sanctnUpdde;
    private Long drafter;
    private SanctnProgress status ;
    private Long atchmnflId;
    private Docform docformSn;
}
