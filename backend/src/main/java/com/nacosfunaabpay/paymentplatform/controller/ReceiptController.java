package com.nacosfunaabpay.paymentplatform.controller;

import com.lowagie.text.DocumentException;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.repositories.ReceiptRepository;
import com.nacosfunaabpay.paymentplatform.service.PdfGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/receipts")
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final ReceiptRepository receiptRepository;
    private final PdfGenerationService pdfGenerationService;

    public ReceiptController(ReceiptRepository receiptRepository,
                             PdfGenerationService pdfGenerationService) {
        this.receiptRepository = receiptRepository;
        this.pdfGenerationService = pdfGenerationService;
    }

    @GetMapping("/generate/{receiptId}")
    public ResponseEntity<?> generatePdf(@PathVariable String receiptId) {
        try {
            Receipt receipt = receiptRepository.findById(Long.valueOf(receiptId))
                    .orElseThrow(() -> new RuntimeException("Receipt not found"));

            Context context = new Context();
            context.setVariable("title", String.format("%s_%s receipt",
                    receipt.getPayment().getStudent().getRegistrationNumber(),
                    receipt.getReceiptDate()));
            context.setVariable("content", String.format("NACOS dues receipt for %s for the %s session",
                    receipt.getPayment().getStudent().getName(),
                    receipt.getPayment().getStudent().getAcademicYear().getYear()));

            byte[] pdfBytes = pdfGenerationService.generatePdf("receipt", context);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename("receipt-" + receipt.getReceiptNumber() + ".pdf")
                    .build());;

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
