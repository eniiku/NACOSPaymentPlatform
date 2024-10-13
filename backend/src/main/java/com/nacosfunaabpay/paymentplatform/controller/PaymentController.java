package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentResponseDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.service.FlutterwaveService;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import com.nacosfunaabpay.paymentplatform.service.PaymentService;
import com.nacosfunaabpay.paymentplatform.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    private final InvoiceService invoiceService;

    private final FlutterwaveService flutterwaveService;

    private final PaymentService paymentService;

    private final ReceiptService receiptService;

    public PaymentController(InvoiceService invoiceService, FlutterwaveService flutterwaveService, PaymentService paymentService, ReceiptService receiptService) {
        this.invoiceService = invoiceService;
        this.flutterwaveService = flutterwaveService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
    }

    @PostMapping("/initialize")
    public ResponseEntity<PaymentResponseDTO> initializePayment(
            @RequestParam("invoice_id") Long invoiceId
    ) {

        try {
            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            invoiceService.sendInvoiceEmail(invoice.getId()); // Send invoice to user's email asynchronously
            String paymentGatewayUrl = flutterwaveService.initializePayment(invoice);

            PaymentResponseDTO response = new PaymentResponseDTO(paymentGatewayUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error initializing payment for invoice: {}", invoiceId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(@RequestParam("transaction_id") String transactionId,
                                                            @RequestParam("invoice_id") Long invoiceId) {

        logger.info("Verifying payment for transaction: {}, invoiceId: {}", transactionId,
                invoiceId);
        try {
            boolean isSuccessful = flutterwaveService.verifyPayment(transactionId);

            if (isSuccessful) {
                logger.info("Payment successful for invoice: {}", invoiceId);
                Invoice invoice = invoiceService.getInvoiceById(invoiceId);
                invoiceService.updateInvoiceStatus(invoiceId, InvoiceStatus.PAID);

                // Create payment record
                Payment payment = paymentService.createPaymentRecord(invoice, transactionId, "Flutterwave");

                // Generate receipt and send email
                Receipt receipt = receiptService.generateReceipt(payment);
                receiptService.sendReceiptEmail(receipt.getId());

                PaymentResponseDTO response = new PaymentResponseDTO("Payment successful", receipt.getId());
                return ResponseEntity.ok(response);
            } else {
                logger.warn("Payment failed for invoice: {}", invoiceId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PaymentResponseDTO("Payment failed", null));
            }
        } catch (IOException e) {
            logger.error("Error verifying payment for invoice: {}", invoiceId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponseDTO("Error verifying payment", null));
        } catch (Exception e) {
            logger.error("Unexpected error during payment verification for invoice: {}", invoiceId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponseDTO("Unexpected error occurred", null));
        }
    }
}
