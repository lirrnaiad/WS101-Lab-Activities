package com.fabia.lab7.service;

import com.fabia.lab7.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Map<String, Object> updates);
    void deleteProduct(Long id);
}
