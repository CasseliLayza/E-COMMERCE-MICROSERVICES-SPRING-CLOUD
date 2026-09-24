package com.backend.notication.kafka;

import com.backend.notication.service.NotificationServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedConsumer.class);
    private final NotificationServiceImp notificationServiceImp;


    public OrderCreatedConsumer(NotificationServiceImp notificationServiceImp) {
        this.notificationServiceImp = notificationServiceImp;
    }


    @KafkaListener(
            topics = "order-created",
            groupId = "notification-service")
    public void consume(OrderCreatedEvent event) {
        logger.info("===============================");
        logger.info("ORDER CREATED EVENT CONSUMED: {}", event);
        logger.info("===============================");

        String message = "Your order with ID " + event.orderId() + " has been created successfully." +
                " Total Price: " + event.totalPrice() +
                " Status: " + event.status() +
                " Created At: " + event.createdAt();

        notificationServiceImp.createdNotification(
                event.userId(),
                event.orderId(),
                message);

    }


}
