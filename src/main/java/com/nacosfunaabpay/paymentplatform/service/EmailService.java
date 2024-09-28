package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;

public interface EmailService {
    void sendInvoiceEmail(Invoice invoice);
}
