package com.nacosfunaabpay.paymentplatform.config;

import io.github.bucket4j.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
//  TODO: rework file to use bucket4jConfig
    public Bucket bucket4jConfiguration() {

        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
