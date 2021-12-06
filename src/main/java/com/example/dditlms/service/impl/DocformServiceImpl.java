package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.repository.sanctn.DocformRepository;
import com.example.dditlms.service.DocformService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocformServiceImpl implements DocformService {

    private final DocformRepository docformRepository;

    @Transactional
    @Override
    public void saveDocform(Docform docform) {
        docformRepository.save(docform);

    }

}
