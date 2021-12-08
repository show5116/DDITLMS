package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.repository.sanctn.SanctnLnRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SanctnLnServiceTest {

    @Autowired
    SanctnLnRepository sanctnLnRepository;

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {

        queryFactory = new JPAQueryFactory(em);
        Member member1 = new Member();
        SanctnLn sanctnLn1 = new SanctnLn();


    }

    @Test
    public void inquire() {

        QueryResults<SanctnLn> result = sanctnLnRepository.inquireProgress(11111L);


    }

}