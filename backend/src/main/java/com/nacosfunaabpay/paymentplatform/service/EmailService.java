package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Receipt;

public interface EmailService {
    void sendInvoiceEmail(Invoice invoice);
    void sendReceiptEmail(Receipt receipt);
}
