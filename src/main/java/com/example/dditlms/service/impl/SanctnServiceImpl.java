package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.entity.sanction.Sanctn;
import com.example.dditlms.domain.repository.sanctn.SanctnRepository;
import com.example.dditlms.service.SanctnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SanctnServiceImpl implements SanctnService {

    private final SanctnRepository sanctnRepository;


    @Transactional
    @Override
    public void saveSanctn(String sanctnSj, Docform docformSn) {

        Sanctn sanctn = new Sanctn();

        sanctn.setSanctnSj(sanctnSj);
        sanctn.setDocformSn(docformSn);

        sanctnRepository.save(sanctn);
    }
}
