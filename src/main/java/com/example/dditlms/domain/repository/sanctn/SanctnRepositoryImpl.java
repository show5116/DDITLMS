package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.QSanctnDTO;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QMember.*;
import static com.example.dditlms.domain.entity.sanction.QSanctn.sanctn;
import static com.example.dditlms.domain.entity.sanction.QSanctnLn.sanctnLn1;

public class SanctnRepositoryImpl implements SanctnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Tuple> showDetail(Long id) {

        return queryFactory
                .select(sanctn, member.userNumber)
                .from(sanctn, member)
                .where(sanctn.sanctnId.eq(id), sanctn.drafter.eq(member.userNumber))
                .fetch();

    }



}
