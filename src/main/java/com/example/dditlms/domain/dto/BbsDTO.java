package com.example.dditlms.domain.dto;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.entity.Bbs;
import com.example.dditlms.domain.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BbsDTO {

    private Long idx;

    private Member member;

    private String title;

    private String content;

    private Date bbsDate;

    private Long bbsCnt;

    private BoardCategory category;

    private String useAt;

    private Long atchmnfId;

    private List<Map<String,String>> tokenList;

    private Long total;

    public Bbs toEntity(){
        Bbs bbs = Bbs.builder()
                .idx(this.idx)
                .title(this.title)
                .content(this.content)
                .bbsDate(this.bbsDate)
                .bbsCnt(this.bbsCnt)
                .category(this.category)
                .atchmnflId(this.atchmnfId)
                .build();
        return bbs;
    }

    @QueryProjection
    public BbsDTO(Long idx, Member member, String title, String content, Date bbsDate, Long bbsCnt, BoardCategory category, String useAt, Long atchmnfId) {
        this.idx = idx;
        this.member = member;
        this.title = title;
        this.content = content;
        this.bbsDate = bbsDate;
        this.bbsCnt = bbsCnt;
        this.category = category;
        this.useAt = useAt;
        this.atchmnfId = atchmnfId;
    }
}
