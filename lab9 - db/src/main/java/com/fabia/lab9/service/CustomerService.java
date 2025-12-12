package com.fabia.lab9.service;

import com.fabia.lab9.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> readAll();
    Optional<Customer> readOne(Long id);
    Customer create(Customer customer);
    Optional<Customer> update(Long id, Customer updatedCustomer);
    boolean delete(Long id);
}
