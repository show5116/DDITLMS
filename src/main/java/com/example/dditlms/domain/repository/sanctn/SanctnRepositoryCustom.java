package com.example.dditlms.domain.repository.sanctn;

import com.querydsl.core.Tuple;

import java.util.List;

public interface SanctnRepositoryCustom {
    public List<Tuple> showDetail(Long id);

}
