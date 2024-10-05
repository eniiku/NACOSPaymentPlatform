package com.nacosfunaabpay.paymentplatform.controller;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("invoiceId")
//@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/generate-invoice")
    public String generateInvoice(@ModelAttribute PaymentFormDTO paymentForm, Model model) {
        Invoice invoice = invoiceService.generateInvoice(paymentForm);

        model.addAttribute("paymentForm", paymentForm);
        model.addAttribute("amount", invoice.getAmountDue());
        model.addAttribute("invoiceId", invoice.getId());
        return "invoice";
    }
}