package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Long> {

}
