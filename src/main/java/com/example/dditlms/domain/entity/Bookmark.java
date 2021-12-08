package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.Menu;
import com.example.dditlms.domain.idclass.BookmarkId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "BKMK")
@Getter
@NoArgsConstructor
@ToString
@IdClass(BookmarkId.class)
public class Bookmark {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="MENU")
    private Menu menu;

    @Id
    @ManyToOne
    @JoinColumn(name = "MBER_NO")
    private Member member;

    @Builder
    public Bookmark(Menu menu, Member member) {
        this.menu = menu;
        this.member = member;
    }
}
