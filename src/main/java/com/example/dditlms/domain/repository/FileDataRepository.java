package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.FileData;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    List<FileData> findAllByMember(Member member);
    Optional<FileData> findByMemberAndParentIsNull(Member member);
    List<FileData> findAllByMemberAndParentIsNull(Member member);
    List<FileData> findAllByMemberAndParent(Member member, FileData parent);
    Optional<FileData> findByFileIdx(Integer fileIdx);
    List<FileData> findAllByMemberAndParentAndExtension(Member member, FileData parent, String extension);
    List<FileData> findAllByMemberAndParentAndExtensionIsNot(Member member, FileData parent, String extension);


//    List<FileData> findAllByFileName(String fileName);
//    List<FileData> findAllByExtensionAndParentId(String extension, int parentId);
//    List<FileData> findAllByExtensionNotAndParentId(String extension, int parentId);
//    List<FileData> findTop4ByParentIdOrderByOpenTimeDesc(int parentId);
//        List<FileData> findAllByParentId(int parentId);
//    List<FileData> save(FileData fileData);
//        FileData findAllById(int fileIdx);
//    FileData findByFileIdx(int fileIdx);
}