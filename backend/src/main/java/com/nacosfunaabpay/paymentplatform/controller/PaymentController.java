package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentResponseDTO;
import com.nacosfunaabpay.paymentplatform.dtos.PaymentVerificationResultDTO;
import com.nacosfunaabpay.paymentplatform.dtos.ReceiptDTO;
import com.nacosfunaabpay.paymentplatform.dtos.TransactionDetailsDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);


    private final InvoiceService invoiceService;
    private final FlutterwaveService flutterwaveService;
    private final PaymentService paymentService;
    private final ReceiptService receiptService;
    private final Mapper<Receipt, ReceiptDTO> receiptMapper;

    public PaymentController(InvoiceService invoiceService,
                             FlutterwaveService flutterwaveService,
                             PaymentService paymentService,
                             ReceiptService receiptService,
                             Mapper<Receipt, ReceiptDTO> receiptMapper) {
        this.invoiceService = invoiceService;
        this.flutterwaveService = flutterwaveService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
        this.receiptMapper = receiptMapper;
    }

    @PostMapping("/initialize")
    public ResponseEntity<PaymentResponseDTO> initializePayment(
            @RequestParam("invoice_no") String invoiceNo
    ) {

        try {
            Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNo);
            invoiceService.sendInvoiceEmail(invoiceNo); // Send invoice to user's email asynchronously
            String paymentGatewayUrl = flutterwaveService.initializePayment(invoice);

            PaymentResponseDTO response = new PaymentResponseDTO(paymentGatewayUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Error initializing payment for invoice: {}", invoiceNo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<PaymentVerificationResultDTO> verifyPayment(@RequestParam("transaction_id") String transactionId,
                                                                                          @RequestParam("trx_ref") String trxRef) {

        String invoiceNo = trxRef.substring(0, trxRef.lastIndexOf('-'));

        logger.info("Verifying payment for transaction: {}, invoiceId: {}", transactionId,
                invoiceNo);
        try {
            PaymentVerificationResultDTO response = flutterwaveService.verifyPayment(transactionId);

            if (response.isSuccessful()) {
                logger.info("Payment successful for invoice: {}", invoiceNo);
                Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNo);
                invoiceService.updateInvoiceStatus(invoiceNo, InvoiceStatus.PAID);

                Map<String, Object> transactionDetails = response.getTransactionDetails();
                String paymentType = (String) transactionDetails.get("payment_type");

                // Create payment record
                Payment payment = paymentService.createPaymentRecord(invoice, transactionId, paymentType);

                // Generate receipt and send email
                Receipt receipt = receiptService.generateReceipt(payment);
                receiptService.sendReceiptEmail(receipt.getId());
                response.setReceipt(receiptMapper.mapTo(receipt));

                return ResponseEntity.ok(response);
            } else {
                logger.warn("Payment failed for invoice: {}", invoiceNo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PaymentVerificationResultDTO(false, null, "Payment failed", null));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during payment verification for invoice: {}", invoiceNo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentVerificationResultDTO(false,null, "Unexpected error occurred", null));
        }
    }
}
