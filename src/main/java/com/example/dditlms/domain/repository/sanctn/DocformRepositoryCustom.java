package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.DocFormDTO;
import com.example.dditlms.domain.entity.sanction.DocFormCategory;

import java.util.List;

public interface DocformRepositoryCustom {

    List<DocFormCategory> allDocFormList();
    List<DocFormDTO> DocFromCate(String cate);

}
