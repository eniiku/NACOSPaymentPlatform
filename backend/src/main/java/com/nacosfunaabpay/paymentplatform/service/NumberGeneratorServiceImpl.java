package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.enums.NumberType;
import com.nacosfunaabpay.paymentplatform.exceptions.ReferenceGenerationException;
import com.nacosfunaabpay.paymentplatform.repositories.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NumberGeneratorServiceImpl implements NumberGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private static final String INVOICE_PREFIX = NumberType.INVOICE.getPrefix();
    private static final String RECEIPT_PREFIX = NumberType.RECEIPT.getPrefix();

    private final InvoiceRepository invoiceRepository;  // Inject repository

    private final AtomicInteger dailyCounter = new AtomicInteger(1);
    private volatile String currentDate;

    public NumberGeneratorServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
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
    @Transactional
    public synchronized String generate(String prefix) {
        try {
            String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

            // Reset counter if it's a new day
            if (!today.equals(currentDate)) {
                initializeCounterForNewDay(today, prefix);
            }

            String referenceNumber;
            boolean isUnique;
            int maxAttempts = 100; // Prevent infinite loop
            int attempts = 0;

            do {
                referenceNumber = String.format("%s%s-%04d",
                        prefix,
                        today,
                        dailyCounter.getAndIncrement());

                // Check if the number exists in the database
                isUnique = !invoiceRepository.existsByInvoiceNumber(referenceNumber);
                attempts++;

                if (attempts >= maxAttempts) {
                    log.error("Failed to generate unique reference number after {} attempts",
                            maxAttempts);
                    throw new RuntimeException("Unable to generate unique reference number");
                }

            } while (!isUnique);

            log.info("Generated unique reference number: {} on attempt {}", referenceNumber,
                    attempts);
            return referenceNumber;

        } catch (Exception e) {
            log.error("Error generating reference number with prefix {}: {}", prefix,
                    e.getMessage());
            throw new ReferenceGenerationException("Failed to generate reference number", e);
        }
    }

    /**
     * Initialize the counter for a new day by finding the highest sequence number used
     */
    private void initializeCounterForNewDay(String today, String prefix) {
        try {
            // Find the highest sequence number for today
            String pattern = prefix + today + "-%";
            Optional<String> lastNumber =
                    invoiceRepository.findTopByInvoiceNumberLikeOrderByInvoiceNumberDesc(pattern);

            if (lastNumber.isPresent()) {
                // Extract and parse the sequence number
                String sequenceStr =
                        lastNumber.get().substring(lastNumber.get().lastIndexOf('-') + 1);
                int sequence = Integer.parseInt(sequenceStr);
                dailyCounter.set(sequence + 1);
            } else {
                dailyCounter.set(1);
            }

            currentDate = today;
            log.info("Daily counter initialized for {}: starting at {}", today,
                    dailyCounter.get());

        } catch (Exception e) {
            log.error("Error initializing daily counter: {}", e.getMessage());
            // Fallback to reset counter
            dailyCounter.set(1);
            currentDate = today;
        }
    }
}
