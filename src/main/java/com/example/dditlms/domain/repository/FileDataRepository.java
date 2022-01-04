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
    Optional<FileData> findByFileIdx(Integer fileIdx);
    List<FileData> findAllByMemberAndParentAndExtension(Member member, FileData parent, String extension);
    List<FileData> findAllByMemberAndParentAndExtensionIsNot(Member member, FileData parent, String extension);

}