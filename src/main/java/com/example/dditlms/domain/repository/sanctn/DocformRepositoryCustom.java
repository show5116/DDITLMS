package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.Docform;

public interface DocformRepositoryCustom {

    String findLastCode(String formCode);

}
