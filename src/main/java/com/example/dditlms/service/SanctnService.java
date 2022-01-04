package com.example.dditlms.service;

import com.example.dditlms.domain.dto.SanctnDTO;
import com.example.dditlms.domain.entity.sanction.Docform;

import java.util.List;
import java.util.Optional;

public interface SanctnService {

    public void saveSanctn(String sanctnSj, Docform docform, Long drafter, String sanctnCn, List<Long> userNumber, Long attFile);
    public void saveComplaint(Docform docform, Long drafter, String sanctnCn, List<Long> userNumber, Long complimentId, String complimentType, Long fileId);
    public Optional<SanctnDTO> viewComplaint(Long id);
    public Optional<SanctnDTO> viewComplaintPro(Long id);
    public List<SanctnDTO> showScholarshipApply(Long userNumber);
}
