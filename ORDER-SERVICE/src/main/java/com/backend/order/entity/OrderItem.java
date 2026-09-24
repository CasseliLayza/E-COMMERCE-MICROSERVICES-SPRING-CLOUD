package com.backend.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    //@ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne()
    @JoinColumn(name = "order_id")
    @JsonIgnore
    //@ToString.Exclude
    private Order order;

}
