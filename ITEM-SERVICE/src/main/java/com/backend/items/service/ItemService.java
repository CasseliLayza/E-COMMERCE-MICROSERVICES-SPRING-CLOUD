package com.backend.items.service;

import com.backend.items.dto.ItemRequest;
import com.backend.items.dto.ItemResponse;

import java.util.List;

public interface ItemService {

    List<ItemResponse> getAllItems();

    ItemResponse getItemById(Long id);
    ItemResponse createItem(ItemRequest itemRequest);
    ItemResponse createItemByProductId(Long productId, ItemRequest itemRequest);

    ItemResponse updateItemByProductId(Long productId, Long itemId, ItemRequest itemRequest);
    ItemResponse updateItem(Long id, ItemRequest itemRequest );
    void deleteItem(Long id);


}
