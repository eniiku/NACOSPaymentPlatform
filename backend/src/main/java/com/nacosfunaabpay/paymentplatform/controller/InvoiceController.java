package com.nacosfunaabpay.paymentplatform.controller;

import com.lowagie.text.DocumentException;
import com.nacosfunaabpay.paymentplatform.dtos.InvoiceResponseDTO;
import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.repositories.InvoiceRepository;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import com.nacosfunaabpay.paymentplatform.service.PdfGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;

    private final Mapper<Invoice, InvoiceResponseDTO> invoiceMapper;
    private final PdfGenerationService pdfGenerationService;
    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceService invoiceService,
                             Mapper<Invoice, InvoiceResponseDTO> invoiceMapper,
                             PdfGenerationService pdfGenerationService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
        this.pdfGenerationService = pdfGenerationService;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody PaymentFormDTO request) {

        Invoice invoice = invoiceService.generateInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceMapper.mapTo(invoice));
    }

    @GetMapping("/generate/{invoiceNo}")
    public ResponseEntity<?> generatePdf(@PathVariable String invoiceNo) {
        try {
            Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNo).orElseThrow(() -> new RuntimeException("Invoice not found: " + invoiceNo));

            Context context = new Context();
            context.setVariable("title", String.format("%s_%s_invoice",
                    invoice.getStudent().getRegistrationNumber(),
                    invoice.getInvoiceDate()));
            context.setVariable("invoice", invoice);
            context.setVariable("student", invoice.getStudent());

            byte[] pdfBytes = pdfGenerationService.generatePdf("invoice", context);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("invoice-" + invoiceNo + ".pdf")
                    .build());
            headers.setCacheControl("no-cache, no-store, must-revalidate");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (DocumentException e) {
            logger.error("Error generating PDF: DocumentException", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error generating PDF: IOException", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error generating PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error generating PDF: " + e.getMessage());
        }
    }

}