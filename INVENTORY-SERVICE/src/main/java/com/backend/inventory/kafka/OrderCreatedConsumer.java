package com.backend.inventory.kafka;

import com.backend.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {
    private Logger logger = LoggerFactory.getLogger(OrderCreatedConsumer.class);
    private final InventoryService inventoryService;

    public OrderCreatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @KafkaListener(
            topics = "order-created",
            groupId = "inventory-service"
    )
    public void consume(OrderCreatedEvent event) {

        logger.info("======================================");
        logger.info("Order Created Event");
        logger.info("Order ID: {}", event.orderId());
        logger.info("User ID: {}", event.userId());
        logger.info("Total Price: {}", event.totalPrice());
        logger.info("======================================");

        for (OrderItemEvent item : event.items()) {

            logger.info("Updating inventory for product: {} with quantity: {}",
                    item.productId(), item.quantity());

            inventoryService.decreaseStock(item.productId(), item.quantity());

        }


    }


}
