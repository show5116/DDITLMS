package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.SanctnDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;

import java.util.List;

public interface SanctnRepositoryCustom {
    List<Tuple> showDetail(Long id);

}
