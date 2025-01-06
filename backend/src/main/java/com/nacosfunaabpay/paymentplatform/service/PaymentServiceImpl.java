package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Transactional
    public Payment createPaymentRecord(Invoice invoice, String transactionReference, String paymentMethod) {

        Optional<Payment> existingPayment = paymentRepository.findByTransactionReference(transactionReference);

        if (existingPayment.isPresent()) {
            logger.info("Payment with transaction reference {} already exists. Returning existing payment.", transactionReference);
            return existingPayment.get();
        }

        logger.info("Creating new payment with transaction reference: {}", transactionReference);


        Payment payment = new Payment();

        payment.setStudent(invoice.getStudent());
        payment.setInvoice(invoice);
        payment.setAmountPaid(invoice.getAmountDue());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentStatus("SUCCESSFUL");
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionReference(transactionReference);
        return paymentRepository.save(payment);
    }
}
