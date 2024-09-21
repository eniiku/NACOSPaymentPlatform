package com.nacosfunaabpay.paymentplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaymentPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentPlatformApplication.class, args);
	}

}
