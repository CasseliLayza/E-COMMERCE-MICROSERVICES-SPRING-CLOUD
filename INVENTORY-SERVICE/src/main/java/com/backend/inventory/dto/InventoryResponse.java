package com.backend.inventory.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Integer stock;
}
