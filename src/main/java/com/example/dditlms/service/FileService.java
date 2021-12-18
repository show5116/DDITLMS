package com.example.dditlms.service;

import com.example.dditlms.domain.dto.FileDataDTO;
import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;

import java.util.List;

public interface FileService {

    FileData addRootFolder(Member member);

    FileData addFolder(FileDataDTO fileDataDTO, Member member);

    void addFiles(List<FileData> fileDataList);
}
