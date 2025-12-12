package com.fabia.lab9.service.impl;

import com.fabia.lab9.model.Invoice;
import com.fabia.lab9.repository.InvoiceRepository;
import com.fabia.lab9.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> readAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Optional<Invoice> readOne(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice create(Invoice newInvoice) {
        newInvoice.setId(null);
        return invoiceRepository.save(newInvoice);
    }

    @Override
    public Optional<Invoice> update(Long id, Invoice updatedInvoice) {
        return invoiceRepository.findById(id)
            .map(existingInvoice -> {
                existingInvoice.setInvoiceDate(updatedInvoice.getInvoiceDate());
                existingInvoice.setTotalAmount(updatedInvoice.getTotalAmount());
                existingInvoice.setCustomer(updatedInvoice.getCustomer());
                existingInvoice.setProducts(updatedInvoice.getProducts());
                return invoiceRepository.save(existingInvoice);
            });
    }

    @Override
    public boolean delete(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
