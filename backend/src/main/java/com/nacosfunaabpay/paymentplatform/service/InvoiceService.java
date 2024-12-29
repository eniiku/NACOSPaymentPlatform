package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import org.springframework.scheduling.annotation.Async;


public interface InvoiceService {
    Invoice generateInvoice(PaymentFormDTO paymentForm);

    Invoice getInvoiceByInvoiceNumber(String invoiceNo);

    void updateInvoiceStatus(String invoiceNo, InvoiceStatus status);

    void sendInvoiceEmail(Invoice invoice);
}
