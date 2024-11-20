package com.nacosfunaabpay.paymentplatform.enums;

public enum NumberType {
    INVOICE("INV"),
    RECEIPT("REC");

    private final String prefix;

    NumberType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
