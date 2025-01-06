package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentVerificationResultDTO;
import com.nacosfunaabpay.paymentplatform.model.Invoice;

import java.io.IOException;
import java.util.Map;

public interface FlutterwaveService {
    String initializePayment(Invoice invoice) throws IOException;

    PaymentVerificationResultDTO verifyPayment(String transactionId) throws RuntimeException;

    PaymentVerificationResultDTO verifyPaymentByReference(String transactionReference) throws RuntimeException;

    String extractTransactionRefFromPaymentDetails(String registrationNo) throws RuntimeException;

}
