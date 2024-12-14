package com.nacosfunaabpay.paymentplatform.service;

import com.lowagie.text.DocumentException;
import org.thymeleaf.context.Context;

import java.io.IOException;

public interface PdfGenerationService {
    byte[] generatePdf(String templateName, Context context) throws DocumentException, IOException;
}
