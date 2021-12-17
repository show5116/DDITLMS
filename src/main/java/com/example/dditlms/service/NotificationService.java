package com.example.dditlms.service;

import com.example.dditlms.domain.entity.Notification;

import java.util.List;

public interface NotificationService {
    public void saveNotifications(List<Notification> notificationList);
    public void deleteNotification(String id);
}
