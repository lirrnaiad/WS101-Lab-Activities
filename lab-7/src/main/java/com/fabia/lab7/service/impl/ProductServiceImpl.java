package com.fabia.lab7.service.impl;

import com.fabia.lab7.model.Product;
import com.fabia.lab7.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {
    private final List<Product> productList = new ArrayList<>(List.of(
            new Product(1L, "iPhone 17 Pro Max 256GB", 92990.99),
            new Product(2L, "Nintendo Switch 2", 26554.50),
            new Product(3L, "iPad Pro M5", 72990.00)
    ));

    private final AtomicLong idCounter;
    private final PriorityQueue<Long> recycledIds = new PriorityQueue<>(); // for IDs deleted by deleteProduct()

    public ProductServiceImpl() {
        long maxId = productList.stream()
                .mapToLong(p -> p.getId() == null ? 0L : p.getId())
                .max()
                .orElse(0L);
        idCounter = new AtomicLong(maxId + 1);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList); // defensive copy
    }

    @Override
    public Product getProductById(Long id) {
        if (id == null) return null;
        return productList.stream()
                .filter(product -> Objects.equals(product.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        String name = product.getName();
        Double price = product.getPrice();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        long newId = recycledIds.isEmpty() ? idCounter.getAndIncrement() : recycledIds.poll();
        Product newProduct = new Product(newId, name, price);
        productList.add(newProduct);
        return newProduct;
    }

    @Override
    public Product updateProduct(Long id, Map<String, Object> updates) {
        if (id == null || updates == null || updates.isEmpty()) {
            return getProductById(id);
        }
        productList.stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst()
                .ifPresent(p -> {
                    if (updates.containsKey("name")) {
                        Object n = updates.get("name");
                        if (n instanceof String && !((String) n).isBlank()) {
                            p.setName((String) n);
                        }
                    }
                    if (updates.containsKey("price")) {
                        Object val = updates.get("price");
                        Double price = null;
                        if (val instanceof Number) {
                            price = ((Number) val).doubleValue();
                        } else if (val != null) {
                            try {
                                price = Double.parseDouble(val.toString());
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        if (price != null && price >= 0) {
                            p.setPrice(price);
                        }
                    }
                });
        return getProductById(id);
    }

    @Override
    public void deleteProduct(Long id) {
        if (id == null) return;
        boolean removed = productList.removeIf(p -> Objects.equals(p.getId(), id));
        if (removed) {
            recycledIds.offer(id);
        }
    }

    @Override
    public String toString() {
        return "ProductServiceImpl{productList=" + productList + '}';
    }
}
