package com.example.dditlms.domain.entity;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.dto.BbsDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BBS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@SequenceGenerator(
        name="BBS_SEQ_GEN",
        sequenceName = "BBS_SEQ",
        initialValue =1
)
public class Bbs {

    @Id
    @Column(name = "BBS_SN")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "BBS_SEQ_GEN")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MBER_NO")
    private Member member;
// 전자게시판 타이틀
    @Column(name = "BBS_SJ")
    private String title;
// 전자게시판 내용
    @Lob
    @Column(name = "BBS_CN")
    private String content;
// 전자게시판 작성일자
    @Column(name = "BBSWRITNG_DE")
    private Date bbsDate;
// 전자게시판 조회수
    @Column(name = "BBS_RDCNT")
    private Long bbsCnt;
// 전자게시판 분류
    @Column(name = "BBS_CL")
    @Enumerated(EnumType.STRING)
    private BoardCategory category;
// 사용여부?
    @Column(name = "USE_AT")
    private String useAt;
// 첨부파일 번호
    @Column(name = "ATCHMNFL_ID")
    private Long atchmnflId;

    public BbsDTO toDTO(){
        BbsDTO bbsDTO = BbsDTO.builder()
                .idx(this.idx)
                .title(this.title)
                .content(this.content)
                .member(this.member)
                .bbsDate(this.bbsDate)
                .bbsCnt(this.bbsCnt)
                .build();
        return bbsDTO;
    }
}
