package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.QMember;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.sanction.QSanctn.sanctn;

public class SanctnRepositoryImpl implements SanctnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Tuple> showDetail(Long id) {

        return queryFactory
                .select(sanctn, QMember.member.userNumber)
                .from(sanctn, QMember.member)
                .where(sanctn.sanctnId.eq(id), sanctn.drafter.eq(QMember.member.userNumber))
                .fetch();

    }


}
