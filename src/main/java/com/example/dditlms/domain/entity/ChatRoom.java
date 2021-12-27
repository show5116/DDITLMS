package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.idclass.ChatMemberId;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Table(name="CHTTROOM")
@Getter
@Setter
@SequenceGenerator(
        name="CHTTROOM_SEQ_GEN",
        sequenceName = "CHTTROOM_SEQ",
        initialValue =1
)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "CHTTROOM_SEQ_GEN")
    @Column(name="CHTTROOM_SN",nullable = false)
    private Long id;

    @Column(name="CHTTROOM_NM",nullable = false)
    private String name;

    @Column(name="RECENT_UPDT_TIME",nullable = false)
    private Date updateTime;

    @Lob
    @Column(name="CHTT_IMG")
    private String chatImg;

}
