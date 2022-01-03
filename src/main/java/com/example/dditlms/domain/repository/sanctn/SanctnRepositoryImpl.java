package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.QSanctnDTO;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QDepartment.*;
import static com.example.dditlms.domain.entity.QEmployee.*;
import static com.example.dditlms.domain.entity.QMember.member;
import static com.example.dditlms.domain.entity.QScholarship.*;
import static com.example.dditlms.domain.entity.sanction.QSanctn.sanctn;
import static com.example.dditlms.domain.entity.sanction.QSanctnLn.*;

public class SanctnRepositoryImpl implements SanctnRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SanctnRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Tuple> showDetail(Long id) {

        return queryFactory
                .select(sanctn, member.userNumber)
                .from(sanctn, member)
                .where(sanctn.sanctnId.eq(id), sanctn.drafter.eq(member.userNumber))
                .fetch();

    }


    //장학금 신청 내역 ID 검색
    @Override
    public List<Long> findScholarshipId(Long id) {

        return queryFactory
                .selectDistinct(sanctn.sanctnId)
                .from(scholarship)
                .innerJoin(sanctnLn1)
                .on(scholarship.student.member.eq(sanctnLn1.mberNo))
                .innerJoin(sanctn)
                .on(sanctn.eq(sanctnLn1.sanctnSn))
                .where(sanctn.sanctnSj.startsWith("장학금"))
                .fetch();

    }

    //장학금 신청내역 조회
    @Override
    public SanctnDTO showScholarshipApplyList(Long id) {

        return queryFactory
                .select(new QSanctnDTO(department.deptNm
                        , employee.employeeRole
                        , member.name
                        , sanctnLn1.sanctnLnProgress
                        , sanctnLn1.sanctnDate
                        , sanctnLn1.sanctnOpinion))
                .from(sanctnLn1)
                .innerJoin(member)
                .on(sanctnLn1.mberNo.eq(member))
                .innerJoin(employee)
                .on(member.eq(employee.member))
                .innerJoin(department)
                .on(employee.deptCode.eq(department))
                .innerJoin(sanctn)
                .on(sanctnLn1.sanctnSn.eq(sanctn))
                .where(sanctn.sanctnId.eq(id))
                .orderBy(sanctnLn1.sanctnDate.desc())
                .orderBy(sanctnLn1.sanctnLn.asc())
                .fetchFirst();
    }





}
