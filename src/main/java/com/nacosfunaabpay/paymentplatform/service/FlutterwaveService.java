package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;

import java.io.IOException;

public interface FlutterwaveService {
    String initializePayment(Invoice invoice) throws IOException;

    boolean verifyPayment(String transactionId) throws IOException;
}
