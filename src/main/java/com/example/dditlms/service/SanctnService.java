package com.example.dditlms.service;

import com.example.dditlms.domain.entity.sanction.Docform;

import java.util.List;

public interface SanctnService {

    public void saveSanctn(String sanctnSj, Docform docform, Long drafter, String sanctnCn, List<Long> userNumber, Long attFile);
}
