package com.example.dditlms.service;

public interface SanctnLnService {
    public void updateSanctnLn(String opinion, Long userNumber, Long id);
    public void rejectSanctnLn(String opinion, Long userNumber, Long id);
    public void lastUpdateSanctnLn(String opinion, Long userNumber, Long id);
    public void lastUpdateComplement(String opinion, Long userNumber, Long id, Long comId);
}
