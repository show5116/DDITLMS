package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.Sanctn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanctnRepository extends JpaRepository <Sanctn, Long> {
}
