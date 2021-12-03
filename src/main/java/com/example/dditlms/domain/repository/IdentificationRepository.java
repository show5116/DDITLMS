package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    Optional<Identification> findByUserNumberAndName(long userNumber,String name);
}
