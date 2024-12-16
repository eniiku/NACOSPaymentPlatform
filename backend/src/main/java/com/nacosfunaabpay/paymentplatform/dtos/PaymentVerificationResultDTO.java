package com.nacosfunaabpay.paymentplatform.dtos;

import java.util.Map;

public class PaymentVerificationResultDTO {
    private final boolean successful;
    private final String status;
    private final String message;
    private ReceiptDTO receipt;
    private final Map<String, Object> transactionDetails;


    // Constructor and getters
    public PaymentVerificationResultDTO(boolean successful, String status, String message, Map<String, Object> transactionDetails) {
        this.successful = successful;
        this.status = status;
        this.message = message;
        this.transactionDetails = transactionDetails;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDTO receipt) {
        this.receipt = receipt;
    }

    public Map<String, Object> getTransactionDetails() {
        return transactionDetails;
    }
}
