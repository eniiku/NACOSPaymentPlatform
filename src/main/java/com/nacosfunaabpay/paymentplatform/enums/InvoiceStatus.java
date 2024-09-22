package com.nacosfunaabpay.paymentplatform.enums;

public enum InvoiceStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    OVERDUE("OVERDUE"),
    CANCELLED("CANCELLED");

    private final String status;

    InvoiceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
