package com.backend.order.dto;

import com.backend.order.entity.OrderItem;
import com.backend.order.entity.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItem> items;
    private Integer itemQuantity;
}
