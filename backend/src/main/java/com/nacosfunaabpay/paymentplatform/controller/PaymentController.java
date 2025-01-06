package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentResponseDTO;
import com.nacosfunaabpay.paymentplatform.dtos.PaymentVerificationResultDTO;
import com.nacosfunaabpay.paymentplatform.dtos.ReceiptDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.mappers.Mapper;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import com.nacosfunaabpay.paymentplatform.service.*;
import io.micrometer.common.util.StringUtils;
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
            logger.debug("Fetching invoice details for invoice number: {}", invoiceNo);
            Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNo);

            if (invoice == null) {
                logger.error("Invoice not found: {}", invoiceNo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(new ErrorResponse("Invoice not found", "No invoice found with number: " + invoiceNo));
            }

            logger.debug("Sending invoice email for invoice number: {}", invoiceNo);
            try {
                invoiceService.sendInvoiceEmail(invoice);
            } catch (Exception e) {
                logger.warn("Email sending failed but continuing with payment initialization: {}", e.getMessage());
            }

            logger.debug("Initializing Flutterwave payment for invoice: {}", invoiceNo);
            String paymentGatewayUrl = flutterwaveService.initializePayment(invoice);

            if (paymentGatewayUrl == null || paymentGatewayUrl.isEmpty()) {
                logger.error("Payment gateway URL is null or empty for invoice: {}", invoiceNo);
//                return ResponseEntity
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(new ErrorResponse("Payment Gateway Error", "Failed to generate payment URL"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            PaymentResponseDTO response = new PaymentResponseDTO(paymentGatewayUrl);
            logger.info("Payment initialization successful for invoice: {}", invoiceNo);

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
                Invoice invoice = invoiceService.updateInvoiceStatus(invoiceNo, InvoiceStatus.PAID);

                Map<String, Object> transactionDetails = response.getTransactionDetails();
                String paymentType = (String) transactionDetails.get("payment_type");

                // Create payment record
                Payment payment = paymentService.createPaymentRecord(invoice, transactionId, paymentType);

                // Generate receipt and send email
                Receipt receipt = receiptService.generateReceipt(payment);
                receiptService.sendReceiptEmail(receipt);
                response.setReceipt(receiptMapper.mapTo(receipt));

                return ResponseEntity.ok(response);
            } else {
                logger.warn("Payment verification failed for invoice: {}", invoiceNo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PaymentVerificationResultDTO(false, "failed", "Payment verification failed", null));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during payment verification for invoice: {}", invoiceNo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentVerificationResultDTO(false, "error", "Unexpected error occurred", null));
        }
    }

    @GetMapping("/verify-by-reference")
    public ResponseEntity<?> verifyPayment(@RequestParam(value = "tx_ref", required = true) String transactionRef) {

        if (StringUtils.isBlank(transactionRef)) {
            return ResponseEntity.badRequest()
                    .body(new PaymentVerificationResultDTO(false, "error", "Transaction reference required", null));
        }

        String invoiceNo = transactionRef.substring(0, transactionRef.lastIndexOf('-'));

        logger.info("Verifying payment for transaction reference: {}, ", transactionRef);
        try {
            PaymentVerificationResultDTO response = flutterwaveService.verifyPaymentByReference(transactionRef);

            if (response.isSuccessful()) {
                logger.info("Payment successful for invoice: {}", invoiceNo);
                Invoice invoice = invoiceService.getInvoiceByInvoiceNumber(invoiceNo);
                invoiceService.updateInvoiceStatus(invoiceNo, InvoiceStatus.PAID);

                Map<String, Object> transactionDetails = response.getTransactionDetails();
                String paymentType = String.valueOf(transactionDetails.get("payment_type"));
                String transactionId = String.valueOf(transactionDetails.get("id"));

                // Create payment record
                Payment payment = paymentService.createPaymentRecord(invoice, transactionId, paymentType);

                // Generate receipt and send email
                Receipt receipt = receiptService.generateReceipt(payment);

                return ResponseEntity.ok(receipt.getId());
            } else {
                logger.warn("Payment verification failed for invoice: {}", invoiceNo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PaymentVerificationResultDTO(false, "failed", "Payment verification failed", null));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during payment verification for transaction reference: {}", transactionRef, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentVerificationResultDTO(false, "error", "Unexpected error occurred", null));
        }
    }

    @GetMapping("retrieve-details")
    public ResponseEntity<?> retrieveTransactionDetails(@RequestParam(value = "reg_no", required = true) String registrationNo) {

        if (StringUtils.isBlank(registrationNo)) {
            return ResponseEntity.badRequest()
                    .body(new PaymentVerificationResultDTO(false, "error", "Matric Number required", null));
        }


        logger.info("Fetching transaction details for student with registration number: {}, ", registrationNo);

        try {
            String response = flutterwaveService.extractTransactionRefFromPaymentDetails(registrationNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Unexpected error while trying to fetch transactions for student with registraton number : {}", registrationNo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentVerificationResultDTO(false, "error", "Unexpected error occurred", null));
        }

    }
}
