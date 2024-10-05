package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
