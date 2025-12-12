package com.fabia.lab9.service.impl;

import com.fabia.lab9.model.Product;
import com.fabia.lab9.repository.ProductRepository;
import com.fabia.lab9.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> readAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> readOne(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product create(Product newProduct) {
        newProduct.setId(null); // Ensure new entity
        return productRepository.save(newProduct);
    }

    @Override
    public Optional<Product> update(Long id, Product updatedProduct) {
        return productRepository.findById(id)
            .map(existingProduct -> {
                existingProduct.setName(updatedProduct.getName());
                existingProduct.setPrice(updatedProduct.getPrice());
                return productRepository.save(existingProduct);
            });
    }

    @Override
    public boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
