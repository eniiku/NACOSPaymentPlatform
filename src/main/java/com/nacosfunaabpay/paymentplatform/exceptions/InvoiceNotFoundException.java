package com.nacosfunaabpay.paymentplatform.exceptions;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(final String message) {
        super(message);
    }
}
