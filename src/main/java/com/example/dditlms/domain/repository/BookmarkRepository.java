package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Bookmark;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Set<Bookmark>> findAllByMember(Member member);
}
