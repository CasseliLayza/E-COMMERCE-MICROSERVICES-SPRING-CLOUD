package com.backend.notication.controller;

import com.backend.notication.dto.NotificationResponse;
import com.backend.notication.service.NotificationServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceImp notificationServiceImp;

    public NotificationController(NotificationServiceImp notificationServiceImp) {
        this.notificationServiceImp = notificationServiceImp;
    }


    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        return new ResponseEntity<>(notificationServiceImp.getAllNotifications(), HttpStatus.OK);

    }

}
