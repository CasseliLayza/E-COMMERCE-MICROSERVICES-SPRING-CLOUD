package com.backend.order.service;

import com.backend.order.client.ItemFeignClient;
import com.backend.order.client.UserFeignClient;
import com.backend.order.dto.ItemResponse;
import com.backend.order.dto.OrderRequest;
import com.backend.order.dto.OrderResponse;
import com.backend.order.dto.UserResponse;
import com.backend.order.entity.Order;
import com.backend.order.entity.OrderItem;
import com.backend.order.entity.model.OrderCreatedEvent;
import com.backend.order.entity.model.OrderItemEvent;
import com.backend.order.entity.model.OrderStatus;
import com.backend.order.exception.ResouceNotFoundException;
import com.backend.order.kafka.OrderEventPublisher;
import com.backend.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ItemFeignClient itemFeignClient;
    private final UserFeignClient userFeignClient;

    private final OrderEventPublisher orderEventPublisher;

    public OrderServiceImp(OrderRepository orderRepository, ModelMapper modelMapper, ItemFeignClient itemFeignClient, UserFeignClient userFeignClient, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.itemFeignClient = itemFeignClient;
        this.userFeignClient = userFeignClient;
        this.orderEventPublisher = orderEventPublisher;
    }


    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        UserResponse userSearch = userFeignClient.getUserById(orderRequest.getUserId());
        if (userSearch == null) {
            throw new ResouceNotFoundException("User not found with id: " + orderRequest.getUserId());
        }

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus(OrderStatus.CREATED);
        order.setItemQuantity(orderRequest.getItems().size());
        order.setCreatedAt(LocalDateTime.now());
        List<Long> items = orderRequest.getItems();

        for (Long itemId : items) {
            ItemResponse item = itemFeignClient.getItemById(itemId);
            if (item == null) {
                throw new ResouceNotFoundException("Item not found with id: " + itemId);
            }
            //OrderItem orderItem = modelMapper.map(item, OrderItem.class);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setTotalPrice(item.getUnitPrice() * item.getQuantity());

            //orderItem.setOrder(order);
            order.addItem(orderItem);
        }

        order.calculateTotalPrice();
        Order savedOrder = orderRepository.save(order);
        OrderCreatedEvent event = mapToEvent(savedOrder);

        orderEventPublisher.publishOrderCreatedEvent(event);


        return modelMapper.map(savedOrder, OrderResponse.class);
    }


    public OrderCreatedEvent mapToEvent(Order order) {
        List<OrderItemEvent> itemEvents = order.getItems().stream()
                .map(item -> new OrderItemEvent(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();

        return new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                itemEvents,
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getCreatedAt()
        );


    }
}