package com.backend.items.controller;

import com.backend.items.dto.ItemRequest;
import com.backend.items.dto.ItemResponse;
import com.backend.items.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
            @RequestMapping("/api/items")
public class ItemController {


    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(itemService.createItem(itemRequest), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(itemService.updateItem(id, itemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ItemResponse> createItemByProductId(@PathVariable Long productId, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(itemService.createItemByProductId(productId, itemRequest), HttpStatus.CREATED);
    }

    @PutMapping("/product/{productId}/item/{itemId}")
    public ResponseEntity<ItemResponse> updateItemByProductId(@PathVariable Long productId, @PathVariable Long itemId, @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<>(itemService.updateItemByProductId(productId, itemId, itemRequest), HttpStatus.OK);
    }


}
