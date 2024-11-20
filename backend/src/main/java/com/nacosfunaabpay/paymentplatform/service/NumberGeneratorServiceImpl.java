package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.enums.NumberType;
import com.nacosfunaabpay.paymentplatform.exceptions.ReferenceGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NumberGeneratorServiceImpl implements NumberGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private static final String INVOICE_PREFIX = NumberType.INVOICE.getPrefix();
    private static final String RECEIPT_PREFIX = NumberType.RECEIPT.getPrefix();

    private final AtomicInteger dailyCounter = new AtomicInteger(1);
    private volatile String currentDate;

    public NumberGeneratorServiceImpl() {
        this.currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    }

    public String generateInvoiceNumber() {
        return generate(INVOICE_PREFIX);
    }

    public String generateReceiptNumber() {
        return generate(RECEIPT_PREFIX);
    }

    /**
     * Generates a unique reference number with format: PREFIX-YYMMDD-SEQUENCE
     * Example: INV231205-0001
     *
     * @param prefix The prefix for the number (e.g., "INV" for Invoice, "REC" for Receipt)
     * @return A unique reference number
     */
    private synchronized String generate(String prefix) {
        try {
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

            // Reset counter if it's a new day
            if (!today.equals(currentDate)) {
                dailyCounter.set(1);
                currentDate = today;
                log.info("Daily counter reset for new day: {}", today);
            }

            return String.format("%s%s-%04d",
                    prefix,
                    today,
                    dailyCounter.getAndIncrement());

        } catch (Exception e) {
            log.error("Error generating reference number with prefix {}: {}", prefix,
                    e.getMessage());
            throw new ReferenceGenerationException("Failed to generate reference number", e);
        }
    }

}
