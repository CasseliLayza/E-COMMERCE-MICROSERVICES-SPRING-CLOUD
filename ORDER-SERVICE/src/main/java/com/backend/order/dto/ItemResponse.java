package com.backend.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemResponse {
    //private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    //private Order order;
}
