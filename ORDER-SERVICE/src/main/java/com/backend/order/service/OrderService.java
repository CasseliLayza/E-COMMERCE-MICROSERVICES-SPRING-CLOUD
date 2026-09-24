package com.backend.order.service;

import com.backend.order.dto.OrderRequest;
import com.backend.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    OrderResponse createOrder(OrderRequest orderRequest);

}
