package com.backend.notication.repository;

import com.backend.notication.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends
        JpaRepository<Notification, Long> {
}
