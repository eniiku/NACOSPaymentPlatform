package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.repositories.ReceiptRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final NumberGeneratorService numberGenerator;
    private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private EmailService emailService;


    public ReceiptServiceImpl(NumberGeneratorService numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public Receipt generateReceipt(Payment payment) {

        Optional<Receipt> existingReceipt = receiptRepository.findByPayment(payment);

        if (existingReceipt.isPresent()) {
            logger.info("Receipt already exists for payment ID: {}. Returning existing receipt.",
                    payment.getId());
            return existingReceipt.get();
        }

        try {
            logger.info("Generating new receipt for payment ID: {}", payment.getId());
            Receipt receipt = new Receipt();
            receipt.setPayment(payment);
            receipt.setReceiptDate(LocalDateTime.now());
            receipt.setReceiptNumber(numberGenerator.generateReceiptNumber());

            Receipt savedReceipt = receiptRepository.save(receipt);
            logger.info("Generated receipt with payment ID: {}", payment.getId());
            return savedReceipt;

        } catch (Exception e) {
            logger.error("Error generating receipt for payment ID: {}", payment.getId(), e);
            throw new RuntimeException("Failed to generate receipt", e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public void sendReceiptEmail(Receipt receipt) {
        emailService.sendReceiptEmail(receipt);
    }
}
