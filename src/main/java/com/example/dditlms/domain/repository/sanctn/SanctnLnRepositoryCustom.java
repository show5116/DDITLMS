package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanctnLnRepositoryCustom {

    List<SanctnDTO> showSanctnLine2(Long id);
    SanctnLn findSanctnId(Long userNumber, Long id);
    SanctnLn findNextSanctnId(Long userNumber, Long id);
    List<SanctnDTO> findRecentOpinion(Long userNumber);
    Page<SanctnLn> inquirePageWithProgress(Long userNumber, Pageable pageable, SanctnProgress sanctnProgress);
    Page<SanctnLn> inquireAll(Long userNumber, Pageable pageable);
}
