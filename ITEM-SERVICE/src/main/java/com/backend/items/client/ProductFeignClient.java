package com.backend.items.client;

import com.backend.items.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${product-service.url}", path = "/api/products", name = "BACKEND-PRODUCTS-SPRING")
public interface ProductFeignClient {


    @GetMapping
    List<ProductResponse> getAllProducts();

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable Long id);


}
