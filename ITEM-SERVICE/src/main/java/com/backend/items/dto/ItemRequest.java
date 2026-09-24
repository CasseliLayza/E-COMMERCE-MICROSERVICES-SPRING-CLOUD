package com.backend.items.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ItemRequest {
    private Long productId;
    private Integer quantity;
    private String serialNumber;
    private Double unitPrice;
    private Double totalPrice;
}
