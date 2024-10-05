package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;

public interface PaymentService {

    Payment createPaymentRecord(Invoice invoice, String transactionReference, String paymentMethod);
}
