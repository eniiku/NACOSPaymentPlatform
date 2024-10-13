package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.InvoiceResponseDTO;
import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final Mapper<Invoice, InvoiceResponseDTO> invoiceMapper;

    public InvoiceController(InvoiceService invoiceService, Mapper<Invoice, InvoiceResponseDTO> invoiceMapper) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody PaymentFormDTO request) {

        Invoice invoice = invoiceService.generateInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceMapper.mapTo(invoice));
    }
}