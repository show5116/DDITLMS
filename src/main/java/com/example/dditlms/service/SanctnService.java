package com.example.dditlms.service;

import com.example.dditlms.domain.dto.DocFormDTO;
import com.example.dditlms.domain.entity.sanction.Docform;

public interface SanctnService {

    public void saveSanctn(String sanctnSj, Docform docformSn);

}
