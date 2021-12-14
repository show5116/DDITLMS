package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.querydsl.core.QueryResults;

import java.util.List;

public interface SanctnLnRepositoryCustom {

    QueryResults<SanctnLn> inquireProgress(Long userNumber);
    QueryResults<SanctnLn> inquireReject(Long userNumber);
    QueryResults<SanctnLn> inquirePublicize(Long userNumber);
    QueryResults<SanctnLn> inquireCompletion(Long userNumber);
    QueryResults<SanctnLn> inquireTotal(Long userNumber);
    List<SanctnDTO> showSanctnLine2(Long id);

    QueryResults<SanctnDTO> showSanctnCount();


}
