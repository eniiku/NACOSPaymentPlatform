package com.nacosfunaabpay.paymentplatform.config;

import com.giffing.bucket4j.spring.boot.starter.context.properties.Bucket4JConfiguration;
import io.github.bucket4j.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket4JConfiguration bucket4jConfiguration() {

        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(10, refill);

        return ((Bucket4JConfiguration) Bucket.builder()
                .addLimit(limit)
                .build());
    }
}
