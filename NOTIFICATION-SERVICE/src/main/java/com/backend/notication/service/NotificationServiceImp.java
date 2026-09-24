package com.backend.notication.service;

import com.backend.notication.dto.NotificationResponse;
import com.backend.notication.entity.Notification;
import com.backend.notication.repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImp {
    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    public NotificationServiceImp(NotificationRepository notificationRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notification -> modelMapper.map(notification, NotificationResponse.class))
                .toList();
    }

    @Transactional
    public Notification createdNotification(
            Long userId, Long orderId, String message) {
        Notification notification = new Notification(userId, orderId, message);
        if (notification.getMessage() == null || notification.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be null or empty");
        }

        return notificationRepository.save(notification);

    }

}