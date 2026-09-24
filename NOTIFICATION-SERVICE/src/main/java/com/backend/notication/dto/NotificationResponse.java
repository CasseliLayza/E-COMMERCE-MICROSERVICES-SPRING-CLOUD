package com.backend.notication.dto;

import com.backend.notication.entity.model.NotificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class NotificationResponse {

    private Long id;
    private Long userId;
    private Long orderId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
