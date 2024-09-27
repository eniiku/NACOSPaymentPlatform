package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/generate-invoice")
    public String generateInvoice(@ModelAttribute PaymentFormDTO paymentForm, Model model) {
        String invoiceNumber = invoiceService.generateInvoice(paymentForm);
        int amount = invoiceService.calculateAmount();
        model.addAttribute("paymentForm", paymentForm);
        model.addAttribute("amount", amount);
        model.addAttribute("invoiceNumber", invoiceNumber);
        return "invoice";
    }
}