package com.backend.products.service.imp;

import com.backend.products.dto.ProductRequest;
import com.backend.products.dto.ProductResponse;
import com.backend.products.entity.Product;
import com.backend.products.exception.DuplicateProductException;
import com.backend.products.exception.ResourceNotFoundException;
import com.backend.products.repository.ProductRepository;
import com.backend.products.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        productRepository.findBySku(productRequest.getSku())
                .ifPresent(product -> {
                    throw new DuplicateProductException("Product with SKU " + productRequest.getSku() + " already exists");
                });


        Product productSaved = productRepository.save(modelMapper.map(productRequest, Product.class));

        return modelMapper.map(productSaved, ProductResponse.class);


    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productsResponse) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productsResponse.getName());
                    product.setDescription(productsResponse.getDescription());
                    product.setPrice(productsResponse.getPrice());
                    product.setSku(productsResponse.getSku());
                    Product updatedProduct = productRepository.save(product);
                    return modelMapper.map(updatedProduct, ProductResponse.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("Product not found with id: " + id);
            }

    }
}
