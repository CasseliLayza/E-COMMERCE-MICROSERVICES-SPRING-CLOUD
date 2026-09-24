package com.backend.order.client;

import com.backend.order.dto.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${item-service.url}", path = "/api/items", name = "ITEM-SERVICE")
public interface ItemFeignClient {

    @GetMapping
    List<ItemResponse> getAllItems();

    @GetMapping("/{id}")
    ItemResponse getItemById(@PathVariable Long id);

}


