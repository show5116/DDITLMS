package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.BoardCategory;
import com.example.dditlms.domain.dto.BbsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BbsRepositoryCustom {
    Page<BbsDTO> pageWithFree(Pageable pageable, BoardCategory boardCategory);
}
