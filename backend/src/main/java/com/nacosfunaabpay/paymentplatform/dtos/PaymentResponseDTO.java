package com.nacosfunaabpay.paymentplatform.dtos;

public class PaymentResponseDTO {
    private String message;
    private String paymentUrl;
    private Long receiptId;

    public PaymentResponseDTO(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public PaymentResponseDTO(String message, Long receiptId) {
        this.message = message;
        this.receiptId = receiptId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }
}
