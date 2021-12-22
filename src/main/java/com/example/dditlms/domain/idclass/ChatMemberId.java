package com.example.dditlms.domain.idclass;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatMemberId implements Serializable {

    private Long chatRoom;

    private Long member;
}
