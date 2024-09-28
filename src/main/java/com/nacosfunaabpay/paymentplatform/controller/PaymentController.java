package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam("invoiceNumber") Long invoiceNumber, @ModelAttribute PaymentFormDTO paymentForm) {
        System.out.println("Invoice number: " + invoiceNumber);
        invoiceService.sendInvoiceEmail(invoiceNumber);

        // Redirect to payment gateway
//        return "redirect:" + paymentGatewayUrl;
        return "redirect:/";
    }
}
