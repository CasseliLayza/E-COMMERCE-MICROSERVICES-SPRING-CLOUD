package com.backend.order.entity.model;

public record OrderItemEvent(
        Long productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice

) {
}
