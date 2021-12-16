package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.FileDataDTO;
import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.repository.FileDataRepository;
import com.example.dditlms.domain.repository.MemberRepository;
import com.example.dditlms.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileDataRepository fileDataRepository;

    @Transactional
    @Override
    public FileData addRootFolder(Member member){
//        Entity에서 할거임 백에서만이니까
        FileData fileData = FileData.builder()
                .member(member)
                .fileName("user"+member.getUserNumber())
                .extension("folder")
                .fileSize(0L)
                .createTime(new Date())
                .openTime(new Date())
                .trash(0)
                .build();

        fileDataRepository.save(fileData);
        return fileData;
    }

    @Transactional
    @Override
    public FileData addFolder(FileDataDTO fileDataDTO, Member member) {
        FileData fileData = fileDataDTO.toEntity();

        Optional<FileData> parentFolderWrapper = fileDataRepository.findByFileIdx(fileDataDTO.getParentId());
        FileData parentFolder = parentFolderWrapper.orElse(null);

        fileData.setMember(member);
        fileData.setParent(parentFolder);
        fileData.setCreateTime(new Date());
        fileDataRepository.save(fileData);

        return fileData;
    }


}
