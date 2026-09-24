package com.backend.notication.entity;

import com.backend.notication.entity.model.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long orderId;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    private LocalDateTime createdAt;


    public Notification(Long userId, Long orderId, String message) {
        this.userId = userId;
        this.orderId = orderId;
        this.message = message;
        this.status = NotificationStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }
}
