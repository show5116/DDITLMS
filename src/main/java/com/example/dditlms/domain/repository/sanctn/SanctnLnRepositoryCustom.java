package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.querydsl.core.QueryResults;

public interface SanctnLnRepositoryCustom {

    QueryResults<SanctnLn> inquireProgress(Long userNumber);
    QueryResults<SanctnLn> inquireReject(Long userNumber);
    QueryResults<SanctnLn> inquirePublicize(Long userNumber);
    QueryResults<SanctnLn> inquireCompletion(Long userNumber);


}
