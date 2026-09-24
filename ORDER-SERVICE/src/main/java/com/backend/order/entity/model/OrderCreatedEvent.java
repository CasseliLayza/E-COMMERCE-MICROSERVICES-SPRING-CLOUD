package com.backend.order.entity.model;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        List<OrderItemEvent> items,
        Double totalPrice,
        String status,
        LocalDateTime createdAt


) {
}
