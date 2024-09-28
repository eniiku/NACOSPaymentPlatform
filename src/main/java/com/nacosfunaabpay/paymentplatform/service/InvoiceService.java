package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.model.Invoice;


public interface InvoiceService {
    Long generateInvoice(PaymentFormDTO paymentForm);

    Invoice getInvoiceById(Long invoiceId);

    void updateInvoiceStatus(Long invoiceId, InvoiceStatus status);

    void sendInvoiceEmail(Long invoiceId);

    int calculateAmount();
}
