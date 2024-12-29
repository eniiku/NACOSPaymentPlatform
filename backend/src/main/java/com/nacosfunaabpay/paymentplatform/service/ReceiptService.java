package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;

public interface ReceiptService {

    Receipt generateReceipt(Payment payment);
    void sendReceiptEmail(Receipt receipt);
}
