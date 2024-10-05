package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
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
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("student", invoice.getStudent());

        String to = invoice.getStudent().getEmail();
        String subject = "Your Invoice for NACOS Payment";
        String templateName = "email/invoice";

        sendEmail(to, subject, templateName, context);
    }

    @Override
    public void sendReceiptEmail(Receipt receipt) {
        Context context = new Context();
        context.setVariable("receipt", receipt);
        context.setVariable("payment", receipt.getPayment());
        context.setVariable("invoice", receipt.getPayment().getInvoice());
        context.setVariable("student", receipt.getPayment().getStudent());

        String to = receipt.getPayment().getStudent().getEmail();
        String subject = "Your Receipt for NACOS Payment";
        String templateName = "email/receipt";

        sendEmail(to, subject, templateName, context);
    }

    private void sendEmail(String to, String subject, String templateName, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = templateEngine.process(templateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
