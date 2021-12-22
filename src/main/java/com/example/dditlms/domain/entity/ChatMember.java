package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.idclass.ChatMemberId;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="CHTTMBER")
@Getter
@Setter
@IdClass(ChatMemberId.class)
public class ChatMember {

    @Id
    @ManyToOne
    @JoinColumn(name="CHTTROOM_SN")
    private ChatRoom chatRoom;

    @Id
    @ManyToOne
    @JoinColumn(name="MBER_NO")
    private Member member;

}
