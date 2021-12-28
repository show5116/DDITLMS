package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.ScholarshipStatus;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Scholarship;
import com.example.dditlms.domain.entity.SemesterByYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScholarshipRepository extends JpaRepository<Scholarship,Long> {
    List<Scholarship> findAllByMemberAndSemesterAndStatusNot(Member member, SemesterByYear semester, ScholarshipStatus status);

    List<Scholarship> findAllByMemberAndStatus(Member member,ScholarshipStatus status);

    List<Scholarship> findAllByMemberAndStatusNot(Member member,ScholarshipStatus status);
}
