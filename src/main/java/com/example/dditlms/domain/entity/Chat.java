package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.ChatStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="CHTT")
@Getter @Setter
@Where(clause = "CHTT_STTUS != 'DELETED'")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CHTT_SN")
    private Long id;

    @Column(name="CHTT_DT")
    private Date chatTime;

    @Column(name="CHTT_CN")
    private String content;

    @Column(name="CHTT_STTUS")
    @ColumnDefault("'NOTREAD'")
    @Enumerated(EnumType.STRING)
    private ChatStatus chatStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHTT_MEBR")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHTTROOM_SN")
    private ChatRoom chatRoom;

}
