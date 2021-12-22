package com.example.dditlms.service;

import com.example.dditlms.domain.dto.FileDataDTO;
import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;

import java.util.List;
import java.util.Map;

public interface FileService {

    FileData addRootFolder(Member member);

    void addFolder(FileDataDTO fileDataDTO, Map<String, Object> map);

    void addFiles(List<FileData> fileDataList);

    void parentChk(Map<String, Object> map);

    void jstree(Map<String, Object> map);

    void changeView(Map<String, Object> map);

    void upload(Map<String, Object> map);

    void tokenService(Map<String, Object> map);

    }
