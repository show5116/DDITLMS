package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.SanctnLn;

import java.util.List;

public interface SanctnLnRepositoryCustom {

    List<SanctnLn> inquire(Long userNumber);
}
