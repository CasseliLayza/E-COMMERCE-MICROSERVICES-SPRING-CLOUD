package com.backend.order.entity;

import com.backend.order.entity.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    private Integer itemQuantity;


    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }


    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }


    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }


}
