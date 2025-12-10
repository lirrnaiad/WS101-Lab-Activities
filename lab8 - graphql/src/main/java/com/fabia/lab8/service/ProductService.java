package com.fabia.lab8.service;

import com.fabia.lab8.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> readAll();
    Optional<Product> readOne(Long id);
    Product create(Product product);
    Optional<Product> update(Long id, Product updatedProduct);
    boolean delete(Long id);
}
