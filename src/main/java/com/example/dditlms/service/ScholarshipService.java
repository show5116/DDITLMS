package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Scholarship;

public interface ScholarshipService {

    public void addScholarship(long fileId,String kindId);

    public void completeScholarship(Scholarship scholarship);

    public void companionScholarship(Scholarship scholarship);
}
