package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.common.MajorSelection;
import com.example.dditlms.domain.entity.Major;

import java.util.List;

public interface MajorRepositoryCustom {

    public List<Major> collegeMajorList(String majorSelection);
}
