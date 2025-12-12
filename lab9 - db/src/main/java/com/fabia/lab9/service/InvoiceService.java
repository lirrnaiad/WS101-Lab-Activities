package com.fabia.lab9.service;

import com.fabia.lab9.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    List<Invoice> readAll();
    Optional<Invoice> readOne(Long id);
    Invoice create(Invoice invoice);
    Optional<Invoice> update(Long id, Invoice updatedInvoice);
    boolean delete(Long id);
}
