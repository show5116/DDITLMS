package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.QSanctnDTO;
import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import com.example.dditlms.domain.entity.sanction.SanctnProgress;
import com.example.dditlms.domain.repository.MemberRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.dditlms.domain.entity.QDepartment.department;
import static com.example.dditlms.domain.entity.QEmployee.employee;
import static com.example.dditlms.domain.entity.QMember.member;
import static com.example.dditlms.domain.entity.sanction.QSanctn.sanctn;
import static com.example.dditlms.domain.entity.sanction.QSanctnLn.sanctnLn1;

@Slf4j
public class SanctnLnRepositoryImpl implements SanctnLnRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final MemberRepository memberRepository;


    public SanctnLnRepositoryImpl(EntityManager entityManager, MemberRepository memberRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.memberRepository = memberRepository;
    }

    @Override
    public QueryResults<SanctnLn> inquireProgress(Long userNumber) {

        Optional<Member> findMember = memberRepository.findByUserNumber(userNumber);

        return queryFactory
                .select(sanctnLn1)
                .from(sanctnLn1)
                .from(sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn), sanctn.status.eq(SanctnProgress.PROGRESS), sanctnLn1.mberNo.eq(findMember.get()))
                .groupBy(sanctnLn1.sanctnSn)
                .fetchResults();
    }

    @Override
    public QueryResults<SanctnLn> inquireReject(Long userNumber) {

        Optional<Member> findMember = memberRepository.findByUserNumber(userNumber);

        return queryFactory
                .select(sanctnLn1)
                .from(sanctnLn1)
                .from(sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn), sanctn.status.eq(SanctnProgress.REJECT), sanctnLn1.mberNo.eq(findMember.get()))
                .groupBy(sanctnLn1.sanctnSn)
                .fetchResults();


    }

    @Override
    public QueryResults<SanctnLn> inquirePublicize(Long userNumber) {

        Optional<Member> findMember = memberRepository.findByUserNumber(userNumber);

        return queryFactory
                .select(sanctnLn1)
                .from(sanctnLn1)
                .from(sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn), sanctn.status.eq(SanctnProgress.PUBLICIZE), sanctnLn1.mberNo.eq(findMember.get()))
                .groupBy(sanctnLn1.sanctnSn)
                .fetchResults();
    }

    @Override
    public QueryResults<SanctnLn> inquireCompletion(Long userNumber) {

        Optional<Member> findMember = memberRepository.findByUserNumber(userNumber);

        return queryFactory
                .select(sanctnLn1)
                .from(sanctnLn1)
                .from(sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn), sanctn.status.eq(SanctnProgress.COMPLETION), sanctnLn1.mberNo.eq(findMember.get()))
                .groupBy(sanctnLn1.sanctnSn)
                .fetchResults();
    }

    @Override
    public QueryResults<SanctnLn> inquireTotal(Long userNumber) {

        Optional<Member> findMember = memberRepository.findByUserNumber(userNumber);

        return queryFactory
                .select(sanctnLn1)
                .from(sanctnLn1)
                .from(sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn), sanctnLn1.mberNo.eq(findMember.get()))
                .groupBy(sanctnLn1.sanctnSn)
                .orderBy(sanctn.sanctnUpdde.desc())
                .fetchResults();
    }



    // 결재라인의견 조회

    @Override
    public List<SanctnDTO> showSanctnLine2(Long id) {

        return queryFactory
                .select(new QSanctnDTO(sanctnLn1.sanctnDate
                        , sanctnLn1.sanctnOpinion
                        , sanctnLn1.lastApproval
                        , member.name
                        , employee.employeeRole
                        , department.deptNm
                        , sanctnLn1.sanctnStep
                        , sanctnLn1.sanctnLnProgress))
                .from(sanctnLn1,
                        member,
                        sanctn,
                        employee,
                        department)
                .join(sanctnLn1.sanctnSn, sanctn)
                .join(sanctnLn1.mberNo, member)
                .join(employee.member, member)
                .join(employee.deptCode, department)
                .where(sanctnLn1.sanctnSn.eq(sanctn)
                        ,sanctnLn1.mberNo.eq(member)
                        ,sanctn.sanctnId.eq(id)
                        , employee.member.eq(member)
                        , employee.deptCode.eq(department))
                .groupBy(member.name)
                .orderBy(sanctnLn1.sanctnStep.asc())
                .fetch();

    }
    // 결재 진행단계 조회

    @Override
    public QueryResults<SanctnDTO> showSanctnCount() {

        return queryFactory
                .select(new QSanctnDTO(sanctnLn1.sanctnStep))
                .from(sanctnLn1, sanctn)
                .join(sanctnLn1.sanctnSn, sanctn)
                .where(sanctnLn1.sanctnSn.eq(sanctn))
                .fetchResults();



    }







    
}
