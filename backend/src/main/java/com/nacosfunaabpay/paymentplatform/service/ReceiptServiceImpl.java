package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.repositories.ReceiptRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final NumberGeneratorService numberGenerator;

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
        Receipt receipt = new Receipt();
        receipt.setPayment(payment);
        receipt.setReceiptDate(LocalDateTime.now());
        receipt.setReceiptNumber(numberGenerator.generateReceiptNumber());
        return receiptRepository.save(receipt);
    }

    @Override
    @Async("asyncExecutor")
    public void sendReceiptEmail(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new RuntimeException("Receipt not found"));
        emailService.sendReceiptEmail(receipt);
    }
}
