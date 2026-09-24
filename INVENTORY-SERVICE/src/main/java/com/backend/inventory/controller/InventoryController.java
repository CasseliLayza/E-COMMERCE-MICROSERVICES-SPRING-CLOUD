package com.backend.inventory.controller;

import com.backend.inventory.dto.InventoryRequest;
import com.backend.inventory.dto.InventoryResponse;
import com.backend.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return new ResponseEntity<>(inventoryService.getAllInventory(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(inventoryService.getInventoryByProductId(productId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@RequestBody InventoryRequest inventoryRequest) {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryRequest), HttpStatus.CREATED);
    }

}
