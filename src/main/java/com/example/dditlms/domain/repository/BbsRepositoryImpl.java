package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.dto.BbsDTO;
import com.example.dditlms.domain.dto.QBbsDTO;
import com.example.dditlms.domain.entity.Bbs;
import com.example.dditlms.domain.entity.QBbs;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QBbs.bbs;

@Slf4j
public class BbsRepositoryImpl implements BbsRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BbsRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<BbsDTO> pageWithFree(Pageable pageable, BoardCategory boardCategory){


        QueryResults<BbsDTO> results = queryFactory
                .selectDistinct(new QBbsDTO(bbs.idx,
                        bbs.member,
                        bbs.title,
                        bbs.content,
                        bbs.bbsDate,
                        bbs.bbsCnt,
                        bbs.category,
                        bbs.useAt,
                        bbs.atchmnflId
                        ))
                .from(bbs)
                .where(bbs.category.eq(boardCategory))
                .orderBy(bbs.bbsDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<BbsDTO> content = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


}
