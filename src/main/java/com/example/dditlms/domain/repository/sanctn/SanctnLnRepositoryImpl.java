package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.QSanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.dditlms.domain.entity.sanction.QSanctnLn.sanctnLn1;

public class SanctnLnRepositoryImpl implements SanctnLnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnLnRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<SanctnLn> inquire(Long userNumber) {

        List<SanctnLn> inquire = queryFactory.selectFrom(sanctnLn1)
                .where(sanctnLn1.mberNo.userNumber.eq(userNumber))
                .where(sanctnLn1.sanctnSttus.eq(String.valueOf(SanctnProgress.PROGRESS))).fetch();

        return inquire;
    }
}
