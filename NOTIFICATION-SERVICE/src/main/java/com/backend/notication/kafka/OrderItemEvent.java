package com.backend.notication.kafka;

public record OrderItemEvent(
        Long productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice

) {
}
