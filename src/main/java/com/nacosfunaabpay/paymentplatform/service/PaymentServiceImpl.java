package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Payment createPaymentRecord(Invoice invoice, String transactionReference, String paymentMethod) {

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
