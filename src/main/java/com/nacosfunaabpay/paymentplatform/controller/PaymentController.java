package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.enums.InvoiceStatus;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.service.FlutterwaveService;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import com.nacosfunaabpay.paymentplatform.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;

@Controller
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FlutterwaveService flutterwaveService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam("invoiceId") Long invoiceId,
                                 @ModelAttribute PaymentFormDTO paymentForm) throws IOException {

        invoiceService.sendInvoiceEmail(invoiceId); // Send invoice to user's email aysnchrounously
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        String paymentGatewayUrl = flutterwaveService.initializePayment(invoice);
        return "redirect:" + paymentGatewayUrl;

    }

    @GetMapping("/payment/verify")
    public String verifyPayment(@RequestParam("transaction_id") String transactionId,
                                @SessionAttribute("invoiceId") Long invoiceId,
                                SessionStatus sessionStatus) {

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

                sessionStatus.setComplete(); // Clear the session attributes
                return "redirect:/payment-success";
            } else {
                logger.warn("Payment failed for invoice: {}", invoiceId);
                return "redirect:/payment-failure";
            }
        } catch (IOException e) {
            logger.error("Error verifying payment for invoice: {}", invoiceId, e);
            return "redirect:/payment-error";
        } catch (Exception e) {
            logger.error("Unexpected error during payment verification for invoice: {}",
                    invoiceId, e);
            return "redirect:/payment-error";
        }
    }
}
