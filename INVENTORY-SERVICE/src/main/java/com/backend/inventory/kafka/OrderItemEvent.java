package com.backend.inventory.kafka;

public record OrderItemEvent(
        Long productId,
        Integer quantity,
        Double unitPrice,
        Double totalPrice

) {

}
