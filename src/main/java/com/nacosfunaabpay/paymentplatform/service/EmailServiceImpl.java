package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendInvoiceEmail(Invoice invoice) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("invoice", invoice);
            String htmlContent = templateEngine.process("email/invoice", context);

            helper.setTo(invoice.getStudent().getEmail());
            helper.setSubject("Your Invoice for NACOS Payment");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the error and possibly retry or notify an admin
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }

}
