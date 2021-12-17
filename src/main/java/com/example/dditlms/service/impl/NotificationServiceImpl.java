package com.example.dditlms.service.impl;

import com.example.dditlms.domain.entity.Notification;
import com.example.dditlms.domain.repository.NotificationRepository;
import com.example.dditlms.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
