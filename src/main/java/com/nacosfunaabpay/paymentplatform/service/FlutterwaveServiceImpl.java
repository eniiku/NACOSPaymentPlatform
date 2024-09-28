package com.nacosfunaabpay.paymentplatform.service;

import com.google.gson.Gson;
import com.nacosfunaabpay.paymentplatform.model.Invoice;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FlutterwaveServiceImpl implements FlutterwaveService {

    private static final Logger logger = LoggerFactory.getLogger(FlutterwaveService.class);

    @Value("${flutterwave.secretKey}")
    private String flutterwaveSecretKey;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public String initializePayment(Invoice invoice) throws IOException {

        logger.info("Initializing payment for invoice: {}", invoice.getId());
        String url = "https://api.flutterwave.com/v3/payments";

        // Dynamically generate the redirect URL
        String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/payment/verify")
                .toUriString();

        Map<String, Object> requestMap = new HashMap<>();
//        TODO: UPDATE INVOICE TABLE TO STORE INVOICE-NO
//        requestMap.put("tx_ref", invoice.getInoviceNumber());
        requestMap.put("tx_ref", invoice.getId());
        requestMap.put("amount", invoice.getAmountDue());
        requestMap.put("currency", "NGN");
        requestMap.put("redirect_url", redirectUrl);
        requestMap.put("payment_options", "card, ussd, banktransfer, account, internetbanking, nqr, applepay, googlepay, enaira, opay");
        requestMap.put("customer", Map.of(
                "email", invoice.getStudent().getEmail(),
                "phonenumber", invoice.getStudent().getPhoneNumber(),
                "name", invoice.getStudent().getName()
        ));
        requestMap.put("customizations", Map.of(
                "title", "NACOS Payment",
                "description", "Payment for NACOS dues"
        ));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), gson.toJson(requestMap)
        );

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + flutterwaveSecretKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);
            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");

            logger.info("Payment initialized successfully for invoice: {}", invoice.getId());
            return (String) dataMap.get("link");
        } catch (IOException e) {
            logger.error("Error initializing payment for invoice: {}", invoice.getId(), e);
            throw e;
            }
        }

    public boolean verifyPayment(String transactionId) throws IOException {

        logger.info("Verifying payment for transaction: {}", transactionId);
        String url = "https://api.flutterwave.com/v3/transactions/" + transactionId + "/verify";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + flutterwaveSecretKey)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);
            Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");

            boolean isSuccessful = "successful".equals(dataMap.get("status"));
            logger.info("Payment verification result for transaction {}: {}", transactionId,
                    isSuccessful ? "successful" : "failed");
            return isSuccessful;
        } catch (IOException e) {
            logger.error("Error verifying payment for transaction: {}", transactionId, e);
            throw e;
        }
    }
}
