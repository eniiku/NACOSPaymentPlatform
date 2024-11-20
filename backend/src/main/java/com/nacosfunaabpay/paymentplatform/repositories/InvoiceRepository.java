package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
