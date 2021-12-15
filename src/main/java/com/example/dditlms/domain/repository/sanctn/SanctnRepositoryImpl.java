package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.QSanctnDTO;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.sanction.QSanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnLnProgress;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QMember.member;
import static com.example.dditlms.domain.entity.sanction.QSanctn.sanctn;
import static com.example.dditlms.domain.entity.sanction.QSanctnLn.*;

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

    @Override
    public QueryResults<SanctnDTO> countSanctn() {

        return queryFactory
                .select(new QSanctnDTO(sanctnLn1.sanctnStep, sanctnLn1.sanctnLnProgress))
                .from(sanctnLn1, sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn))
                .groupBy(sanctn.sanctnId)
                .fetchResults();
    }


}
