package com.fabia.lab8.service.impl;

import com.fabia.lab8.model.Product;
import com.fabia.lab8.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {
    private Map<Long, Product> products = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(0);

    public ProductServiceImpl() {
        long id1 = idGenerator.incrementAndGet();
        products.put(id1, new Product(id1, "iPhone 17 Pro Max 256GB", 92990.99));
        long id2 = idGenerator.incrementAndGet();
        products.put(id2, new Product(id2, "Nintendo Switch 2", 26554.50));
        long id3 = idGenerator.incrementAndGet();
        products.put(id3, new Product(id3, "iPad Pro M5", 72990.00));
    }

    @Override
    public List<Product> readAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> readOne(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Product create(Product newProduct) {
        long newId = idGenerator.incrementAndGet();
        newProduct.setId(newId);
        products.put(newId, newProduct);
        return newProduct;
    }

    @Override
    public Optional<Product> update(Long id, Product updatedProduct) {
        if (products.containsKey(id)) {
            Product oldProduct = products.get(id);
            oldProduct.setName(updatedProduct.getName());
            oldProduct.setPrice(updatedProduct.getPrice());
            return Optional.of(oldProduct);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return products.remove(id) != null;
    }
}
