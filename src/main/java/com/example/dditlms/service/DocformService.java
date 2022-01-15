package com.example.dditlms.service;


import com.example.dditlms.domain.entity.sanction.Docform;

public interface DocformService {

    void saveDocform(Docform docform);
    String replaceDocform(String docformCn);
}
