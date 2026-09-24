package com.backend.inventory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long productId;
    private Integer stock;


    public void decreaseStock(Integer quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        stock -= quantity;
    }

    public void increaseStock(Integer quantity) {
        stock += quantity;
    }


}
