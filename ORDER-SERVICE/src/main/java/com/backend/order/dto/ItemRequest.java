package com.backend.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {
    private Long productId;
    private Integer quantity;
    private String serialNumber;
    private Double unitPrice;
    private Double totalPrice;
}
