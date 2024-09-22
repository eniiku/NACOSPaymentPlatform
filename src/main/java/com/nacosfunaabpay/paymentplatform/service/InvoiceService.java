package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.model.Invoice;


public interface InvoiceService {
    String generateInvoice(PaymentFormDTO paymentForm);

    Invoice getInvoiceById(Long invoiceId);

    void updateInvoiceStatus(Long invoiceId, InvoiceStatus status);

    int calculateAmount();
}
