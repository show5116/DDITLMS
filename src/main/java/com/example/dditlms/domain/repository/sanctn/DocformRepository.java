package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.Docform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocformRepository extends JpaRepository<Docform, Long>, DocformRepositoryCustom {

}
