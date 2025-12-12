package com.fabia.lab9.controller;

import com.fabia.lab9.model.Product;
import com.fabia.lab9.service.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQLController {
    private ProductService productService;
    public GraphQLController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> findAllProducts() {
        return productService.readAll();
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return productService.readOne(id).orElse(null);
    }

    @MutationMapping
    public Product createProduct(@Argument String name, @Argument Double price) {
        Product newProduct = Product.builder()
                .name(name)
                .price(price)
                .build();
        return productService.create(newProduct);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument String name, @Argument Double price) {
        Product updateDetails = Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
        return productService.update(id, updateDetails).orElse(null);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        return productService.delete(id);
    }
}