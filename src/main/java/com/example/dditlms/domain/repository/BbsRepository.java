package com.example.dditlms.domain.repository;


import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.entity.Bbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BbsRepository extends JpaRepository<Bbs, Integer> {
    Optional<Bbs> findByIdx(Long idx);
    Optional<List<Bbs>> findAllByCategory(BoardCategory boardCategory);
    Optional<List<Bbs>> findAllByCategoryOrderByBbsDateDesc(BoardCategory boardCategory);

}
