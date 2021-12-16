package com.example.dditlms.service;

public interface SanctnLnService {
    public void updateSanctnLn(String opinion, Long userNumber, Long id);
    public void rejectSanctnLn(String opinion, Long userNumber, Long id);
    public void lastUpadteSanctnLn(String opinion, Long userNumber, Long id);
}
