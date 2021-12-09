package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {
    Optional<MemberDetail> findByMember(Member member);
}
