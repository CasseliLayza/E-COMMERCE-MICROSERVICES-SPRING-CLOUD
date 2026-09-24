package com.backend.inventory.service;

import com.backend.inventory.dto.InventoryRequest;
import com.backend.inventory.dto.InventoryResponse;
import com.backend.inventory.entity.Inventory;
import com.backend.inventory.repository.InventoryRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    public InventoryService(InventoryRepository inventoryRepository, ModelMapper modelMapper) {
        this.inventoryRepository = inventoryRepository;
        this.modelMapper = modelMapper;
        configMapper();
    }

    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {

        logger.info("Creating inventory for productId: {} with initial stock: {}",
                inventoryRequest.getProductId(), inventoryRequest.getStock());

        return modelMapper.map(
                inventoryRepository.save(
                        modelMapper.map(
                                inventoryRequest, Inventory.class
                        )
                ), InventoryResponse.class
        );
    }

    public void increaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for productId: " + productId
                        )
                );

        inventory.increaseStock(quantity);
        inventoryRepository.save(inventory);

        logger.info("Increased stock for productId: {} by quantity: {}. New stock: {}"
                , productId, quantity, inventory.getStock());

    }


    public void decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for productId: " + productId
                        )
                );

        inventory.decreaseStock(quantity);
        inventoryRepository.save(inventory);

        logger.info("Decreased stock for productId: {} by quantity: {}. New stock: {}"
                , productId, quantity, inventory.getStock());

    }

    public Integer getStock(Long productId) {
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for productId: " + productId
                        )
                );

        return inventory.getStock();
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        return inventoryRepository
                .findByProductId(productId)
                .map(inventory -> modelMapper.map(inventory, InventoryResponse.class))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for productId: " + productId
                        )
                );


    }

    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories.stream()
                .map(inventory -> modelMapper.map(inventory, InventoryResponse.class))
                .toList();
    }

    public void configMapper() {
        modelMapper.typeMap(InventoryRequest.class, Inventory.class)
                .addMappings(mapper -> mapper.skip(Inventory::setId));
    }

}
