package com.example.dditlms.domain.repository;

import com.example.dditlms.domain.entity.Member;
import com.example.dditlms.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByMember(Member member);
}
