package com.backend.items.service;

import com.backend.items.client.ProductFeignClient;
import com.backend.items.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceFeign {


    private final ProductFeignClient productFeignClient;


    public ProductServiceFeign(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }


    public List<ProductResponse> getAllProducts() {
        return productFeignClient.getAllProducts();
    }

    public ProductResponse getProductById(Long id) {
        return productFeignClient.getProductById(id);
    }


}
