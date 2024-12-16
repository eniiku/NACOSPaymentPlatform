package com.nacosfunaabpay.paymentplatform.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class TransactionDetailsDTO {
    private Long id;
    private String txRef;
    private String currency;
    private BigDecimal amount;
    private BigDecimal chargedAmount;
    private BigDecimal appFee;
    private String status;
    private String paymentType;
    private LocalDateTime createdAt;
    private Map<String, String> metaData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxRef() {
        return txRef;
    }

    public void setTxRef(String txRef) {
        this.txRef = txRef;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(BigDecimal chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public BigDecimal getAppFee() {
        return appFee;
    }

    public void setAppFee(BigDecimal appFee) {
        this.appFee = appFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }
}
