package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.example.dditlms.domain.entity.sanction.QSanctnLn.sanctnLn1;

public class SanctnLnRepositoryImpl implements SanctnLnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnLnRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public QueryResults<SanctnLn> inquireProgress(Long userNumber) {

        return queryFactory
                .selectFrom(sanctnLn1)
                .where(sanctnLn1.mberNo.userNumber.eq(userNumber)
                        ,(sanctnLn1.sanctnSttus.eq(String.valueOf(SanctnProgress.PROGRESS))))
                .fetchResults();
    }

    @Override
    public QueryResults<SanctnLn> inquireReject(Long userNumber) {

        return queryFactory
                .selectFrom(sanctnLn1)
                .where(sanctnLn1.mberNo.userNumber.eq(userNumber)
                        ,(sanctnLn1.sanctnSttus.eq(String.valueOf(SanctnProgress.REJECT))))
                .fetchResults();

    }

    @Override
    public QueryResults<SanctnLn> inquirePublicize(Long userNumber) {
        return queryFactory
                .selectFrom(sanctnLn1)
                .where(sanctnLn1.mberNo.userNumber.eq(userNumber)
                        ,(sanctnLn1.sanctnSttus.eq(String.valueOf(SanctnProgress.PUBLICIZE))) )
                .fetchResults();
    }

    @Override
    public QueryResults<SanctnLn> inquireCompletion(Long userNumber) {
        return queryFactory
                .selectFrom(sanctnLn1)
                .where(sanctnLn1.mberNo.userNumber.eq(userNumber)
                        ,(sanctnLn1.sanctnSttus.eq(String.valueOf(SanctnProgress.COMPLETION))))
                .fetchResults();
    }


}
