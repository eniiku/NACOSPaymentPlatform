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

    private final NumberGeneratorService numberGenerator;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EmailService emailService;

    public InvoiceServiceImpl(NumberGeneratorService numberGenerator) {
        this.numberGenerator = numberGenerator;
    }

    @Override
    @Transactional
    public Invoice generateInvoice(PaymentFormDTO paymentForm) {
        logger.info("Generating invoice for student: {}", paymentForm.getEmail());

        Student student = studentService.handleStudentRegistration(paymentForm);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(numberGenerator.generateInvoiceNumber());
        invoice.setStudent(student);
        invoice.setAmountDue(student.getLevel().getDuesAmount());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setInvoiceStatus(InvoiceStatus.PENDING.getStatus());
        invoice.setDueDate(LocalDate.now().plusDays(7)); // Due in 7 days
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        logger.info("Invoice generated successfully. Invoice ID: {}", savedInvoice.getId());

        return savedInvoice;
    }

    @Override
    public Invoice getInvoiceByInvoiceNumber(String invoiceNo) {
        logger.debug("Fetching invoice with ID: {}", invoiceNo);
        return invoiceRepository.findByInvoiceNumber(invoiceNo)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with ID: " + invoiceNo));
    }

    @Override
    @Transactional
    public void updateInvoiceStatus(String invoiceNo, InvoiceStatus status) {
        logger.info("Updating invoice status. Invoice ID: {}, New status: {}", invoiceNo, status);
        Invoice invoice = getInvoiceByInvoiceNumber(invoiceNo);
        invoice.setInvoiceStatus(status.getStatus());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }

    @Override
    @Async("asyncExecutor")
    public void sendInvoiceEmail(Invoice invoice) {
        emailService.sendInvoiceEmail(invoice);
    }
}
