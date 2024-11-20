package com.nacosfunaabpay.paymentplatform.exceptions;

@SuppressWarnings("serial")
public class ReferenceGenerationException extends RuntimeException {
    public ReferenceGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
