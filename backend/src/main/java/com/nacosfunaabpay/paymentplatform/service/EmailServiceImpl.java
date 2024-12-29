package com.nacosfunaabpay.paymentplatform.service;

import com.lowagie.text.DocumentException;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;


@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private final PdfGenerationService pdfGenerationService;

    public EmailServiceImpl(PdfGenerationService pdfGenerationService) {
        this.pdfGenerationService = pdfGenerationService;
    }

    @Override
    public void sendInvoiceEmail(Invoice invoice) {
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("student", invoice.getStudent());

        String to = invoice.getStudent().getEmail();
        String subject = "Here's Your Invoice for NACOS Dues Payment";
        String templateName = "email/invoice";
        String pdfFileName = String.format("%s_invoice", invoice.getStudent().getRegistrationNumber());

        sendEmail(to, subject, templateName, context, "invoice", pdfFileName);
    }

    @Override
    public void sendReceiptEmail(Receipt receipt) {
        Context context = new Context();
        context.setVariable("receipt", receipt);

        String to = receipt.getPayment().getStudent().getEmail();
        String subject = "Here's Your Receipt for NACOS Payment";
        String templateName = "email/receipt";
        String pdfFileName = String.format("%s_receipt", receipt.getPayment().getStudent().getRegistrationNumber());

        sendEmail(to, subject, templateName, context, "receipt", pdfFileName);
    }

    private void sendEmail(String to,
                           String subject,
                           String emailTemplateName,
                           Context context,
                           String pdfTemplateName,
                           String pdfFileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = templateEngine.process(emailTemplateName, context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            byte[] pdfContent = pdfGenerationService.generatePdf(pdfTemplateName, context);
            helper.addAttachment(pdfFileName, new ByteArrayResource(pdfContent));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
