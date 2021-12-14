package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Optional<Attachment> findFirstByOrderByIdDesc();
    Optional<Attachment> findByIdAndOrder(Long id, Integer order);
    List<Attachment> findAllById(Long id);
}
