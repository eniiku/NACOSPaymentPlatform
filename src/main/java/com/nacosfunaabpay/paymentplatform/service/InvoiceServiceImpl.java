package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.exceptions.InvoiceNotFoundException;
import com.nacosfunaabpay.paymentplatform.exceptions.StudentNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Student;
import com.nacosfunaabpay.paymentplatform.repositories.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public Long generateInvoice(PaymentFormDTO paymentForm) {
        logger.info("Generating invoice for student: {}", paymentForm.getEmail());

        Student student;
        try {
            student = studentService.findStudentByRegistrationNumber(paymentForm.getRegistrationNumber());
        } catch (StudentNotFoundException e) {
            student = studentService.createStudent(paymentForm);
        }

        Invoice invoice = new Invoice();
        invoice.setStudent(student);
        invoice.setAmountDue(student.getLevel().getDuesAmount());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setInvoiceStatus(InvoiceStatus.PENDING.getStatus());
        invoice.setDueDate(LocalDate.now().plusDays(7)); // Due in 7 days
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        logger.info("Invoice generated successfully. Invoice ID: {}", savedInvoice.getId());

        return savedInvoice.getId();
    }

    @Override
    @Transactional
    public Invoice getInvoiceById(Long invoiceId) {
        logger.debug("Fetching invoice with ID: {}", invoiceId);
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + invoiceId));
    }

    @Override
    @Transactional
    public void updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        logger.info("Updating invoice status. Invoice ID: {}, New status: {}", invoiceId, status);
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.setInvoiceStatus(status.getStatus());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }

    @Override
    @Async("asyncExecutor")
    public void sendInvoiceEmail(Long invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        emailService.sendInvoiceEmail(invoice);
    }

    // TODO: Refactor
    public int calculateAmount() {
        return 5000;
    }
}
