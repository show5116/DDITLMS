package com.example.dditlms.domain.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long userNumber;

    private String name;

    private String major;

    private Long payment;

    private Long realPayment;
}
