package com.nacosfunaabpay.paymentplatform.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.nacosfunaabpay.paymentplatform.dtos.PaymentVerificationResultDTO;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import io.micrometer.common.util.StringUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class FlutterwaveServiceImpl implements FlutterwaveService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);


    @Value("${flutterwave.secretKey}")
    private String flutterwaveSecretKey;

    private final OkHttpClient client;
    private final Gson gson;

    private final String FLUTTERWAVE_BASE_URL = "https://api.flutterwave.com/v3";


    public FlutterwaveServiceImpl() {
        // Configure OkHttpClient with timeouts and connection pool
        this.client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();

        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    public String initializePayment(Invoice invoice) throws RuntimeException {
        validateInvoice(invoice);

        logger.info("Initializing payment for invoice: {}", invoice.getInvoiceNumber());

        String url = String.format("%s/payments", FLUTTERWAVE_BASE_URL);

        try {
            Map<String, Object> requestMap = createPaymentRequest(invoice);
            RequestBody body = createRequestBody(requestMap);
            Request request = createHttpRequest(url, body);

            return executeRequest(request, invoice.getInvoiceNumber());

        } catch (Exception e) {
            String errorMessage = String.format("Failed to initialize payment for invoice: %s", invoice.getInvoiceNumber());
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private void validateInvoice(Invoice invoice) throws RuntimeException {
        if (invoice == null) {
            throw new RuntimeException("Invoice cannot be null");
        }
        if (invoice.getStudent() == null) {
            throw new RuntimeException("Invoice must have a student");
        }
        if (invoice.getAmountDue() == null) {
            throw new RuntimeException("Invalid amount due");
        }
    }

    private Map<String, Object> createPaymentRequest(Invoice invoice) {
        String txRef = generateTransactionReference(invoice.getInvoiceNumber());

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("tx_ref", txRef);
        requestMap.put("amount", invoice.getAmountDue());
        requestMap.put("currency", "NGN");
        String REDIRECT_URL = "http://localhost:5173/verify";
        requestMap.put("redirect_url", REDIRECT_URL);
        requestMap.put("payment_options", "card,ussd,banktransfer,account,internetbanking,nqr,applepay,googlepay,enaira,opay");

        // Customer details
        Map<String, String> customer = new HashMap<>();
        customer.put("email", invoice.getStudent().getEmail());
        customer.put("phonenumber", invoice.getStudent().getPhoneNumber());
        customer.put("name", invoice.getStudent().getName());
        requestMap.put("customer", customer);

        // Customizations
        Map<String, String> customizations = new HashMap<>();
        customizations.put("title", "NACOS Payment");
        customizations.put("description", String.format("Payment for NACOS dues - Invoice %s", invoice.getInvoiceNumber()));
        requestMap.put("customizations", customizations);

        // Add meta data for better tracking
        Map<String, String> meta = new HashMap<>();
        meta.put("invoice_number", invoice.getInvoiceNumber());
        meta.put("student_id", invoice.getStudent().getId().toString());
        requestMap.put("meta", meta);

        return requestMap;
    }

    private String generateTransactionReference(String invoiceNumber) {
        return String.format("%s-%s", invoiceNumber, UUID.randomUUID().toString().substring(0, 8));
    }

    private RequestBody createRequestBody(Map<String, Object> requestMap) {
        return RequestBody.create(gson.toJson(requestMap), MediaType.parse("application/json"));
    }

    private Request createHttpRequest(String url, RequestBody body) {
        return new Request.Builder().url(url).addHeader("Authorization", "Bearer " + flutterwaveSecretKey).addHeader("Content-Type", "application/json").post(body).build();
    }

    private String executeRequest(Request request, String invoiceNumber) throws IOException, RuntimeException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                handleUnsuccessfulResponsePaymentInitialization(response, invoiceNumber);
            }

            return extractPaymentLink(response, invoiceNumber);
        }
    }

    private void handleUnsuccessfulResponsePaymentInitialization(Response response, String invoiceNumber) throws IOException, RuntimeException {
        String responseBody = response.body() != null ? response.body().string() : "No response body";
        String errorMessage = String.format("Payment initialization failed for invoice %s. Status: %d, Body: %s", invoiceNumber, response.code(), responseBody);

        logger.error(errorMessage);
        throw new RuntimeException((errorMessage));
    }

    private String extractPaymentLink(Response response, String invoiceNumber) throws IOException, RuntimeException {
        try {
            String responseBody = response.body().string();
            Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);

            // Verify response status
            String status = (String) responseMap.get("status");
            if (!"success".equals(status)) {
                throw new RuntimeException(("Payment initialization failed: " + responseMap.get("message")));
            }

            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
            if (dataMap == null || !dataMap.containsKey("link")) {
                throw new RuntimeException(("Invalid response format: missing payment link"));
            }

            String paymentLink = (String) dataMap.get("link");
            logger.info("Payment link generated successfully for invoice: {}", invoiceNumber);

            return paymentLink;

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Failed to parse Flutterwave response", e);
        }
    }

    public PaymentVerificationResultDTO verifyPayment(String transactionId) throws RuntimeException {
        if (StringUtils.isBlank(transactionId)) {
            throw new RuntimeException("Transaction ID cannot be empty");
        }

        logger.info("Initiating payment verification for transaction: {}", transactionId);
        String url = String.format("%s/transactions/%s/verify", FLUTTERWAVE_BASE_URL, transactionId);

        Request request = createVerificationRequest(url);

        try (Response response = executeRequest(request)) {
            return processVerificationResponse(response, transactionId);
        } catch (Exception e) {
            handleVerificationError(transactionId, e);
            throw new RuntimeException("Payment verification failed", e);
        }
    }

    private Request createVerificationRequest(String url) {
        return new Request
                .Builder().url(url)
                .addHeader("Authorization", "Bearer " + flutterwaveSecretKey)
                .addHeader("Content-Type", "application/json")
                .get().build();
    }

    private Response executeRequest(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    private PaymentVerificationResultDTO processVerificationResponse(Response response, String transactionId) throws IOException, RuntimeException {

        if (!response.isSuccessful()) {
            handleUnsuccessfulResponse(response, transactionId);
        }

        String responseBody = response.body() != null ? response.body().string() : null;
        if (responseBody == null) {
            throw new RuntimeException("Empty response from payment gateway");
        }

        try {
            Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);

            // Validate response structure
            if (responseMap == null || !responseMap.containsKey("data")) {
                throw new RuntimeException("Invalid response format from payment gateway");
            }

            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
            String status = Objects.toString(dataMap.get("status"), "").toLowerCase();

            boolean isSuccessful = "successful".equals(status);

            // Log transaction details
            logger.info("Payment verification completed for transaction {}: Status={}", transactionId, status);

            String message = isSuccessful ? "Payment verified successfully" : "Payment verification failed: " + status;

            return new PaymentVerificationResultDTO(isSuccessful, status, message, dataMap);

        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Failed to parse payment gateway response", e);
        }
    }

    private void handleUnsuccessfulResponse(Response response, String transactionId) throws IOException, RuntimeException {

        String errorBody = response.body() != null ? response.body().string() : "No response body";
        String errorMessage = String.format("Payment verification failed. Status: %d, Body: %s", response.code(), errorBody);

        logger.error("Unsuccessful verification response for transaction {}: {}", transactionId, errorMessage);

        throw new RuntimeException(errorMessage);
    }

    private void handleVerificationError(String transactionId, Exception e) {
        logger.error("Error during payment verification for transaction {}: {}", transactionId, e.getMessage(), e);
    }
}
