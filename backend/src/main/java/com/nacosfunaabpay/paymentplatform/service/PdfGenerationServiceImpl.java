package com.nacosfunaabpay.paymentplatform.service;

import com.lowagie.text.DocumentException;
import org.aspectj.apache.bcel.util.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGenerationService.class);

    private final TemplateEngine templateEngine;

    public PdfGenerationServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, Context context) throws DocumentException, IOException {
        try {
            logger.info("Generating PDF from template: {}", templateName);

            ClassPathResource resource = new ClassPathResource("static/images/logo.png");
            String logoPath = resource.getFile().getAbsolutePath();
            context.setVariable("logoSrc", "file:///" + logoPath);

            String htmlContent = templateEngine.process(String.valueOf(new ClassPath("pdf/" + templateName)), context);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            logger.info("PDF generated successfully");
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Error generating PDF", e);
            throw e;
        }
    }

}
