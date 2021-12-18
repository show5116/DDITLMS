package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Notification;
import com.example.dditlms.domain.repository.NotificationRepository;
import com.example.dditlms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void saveNotifications(List<Notification> notificationList) {
        notificationRepository.saveAll(notificationList);
    }

    @Override
    public void deleteNotification(String id) {
        Optional<Notification> notificationWrapper = notificationRepository.findById(Long.parseLong(id));
        Notification notification = notificationWrapper.orElse(null);
        notification.setDelete('Y');
        notificationRepository.save(notification);
    }
}
