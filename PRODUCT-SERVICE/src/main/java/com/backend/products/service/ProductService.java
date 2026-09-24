package com.backend.products.service;

import com.backend.products.dto.ProductRequest;
import com.backend.products.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductRequest productsResponse);
    ProductResponse updateProduct(Long id, ProductRequest productsResponse );
    void deleteProduct(Long id);


}
