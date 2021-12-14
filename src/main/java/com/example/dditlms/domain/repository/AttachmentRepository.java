package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Optional<Attachment> findFirstByOrderByIdDesc();
}
