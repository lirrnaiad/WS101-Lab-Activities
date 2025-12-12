package com.fabia.lab9.service.impl;

import com.fabia.lab9.model.Customer;
import com.fabia.lab9.repository.CustomerRepository;
import com.fabia.lab9.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> readAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> readOne(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer create(Customer newCustomer) {
        newCustomer.setId(null);
        return customerRepository.save(newCustomer);
    }

    @Override
    public Optional<Customer> update(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id)
            .map(existingCustomer -> {
                existingCustomer.setName(updatedCustomer.getName());
                existingCustomer.setEmail(updatedCustomer.getEmail());
                existingCustomer.setPhone(updatedCustomer.getPhone());
                return customerRepository.save(existingCustomer);
            });
    }

    @Override
    public boolean delete(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
