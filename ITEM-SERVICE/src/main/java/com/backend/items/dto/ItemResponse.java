package com.backend.items.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class ItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String serialNumber;
    private Double unitPrice;
    private Double totalPrice;
}