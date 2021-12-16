package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.sanction.Sanctn;
import com.example.dditlms.domain.entity.sanction.SanctnLn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctnLnRepository extends JpaRepository<SanctnLn, Long>, SanctnLnRepositoryCustom {

}
