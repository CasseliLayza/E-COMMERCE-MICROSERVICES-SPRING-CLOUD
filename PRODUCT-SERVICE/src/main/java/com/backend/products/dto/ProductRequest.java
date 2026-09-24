package com.backend.products.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductRequest {

    private String name;
    private String description;
    private Double price;
    private String sku;

}



