package com.backend.products.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String sku;

}


