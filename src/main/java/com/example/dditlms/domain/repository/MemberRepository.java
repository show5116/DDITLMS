package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.Role;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByUserNumber(long userNumber);
    Optional<Member> findByUserNumberAndName(long userNumber,String name);
    Optional<List<Member>> findAllByRoleAndMemberIdIsNull(Role role);
}
