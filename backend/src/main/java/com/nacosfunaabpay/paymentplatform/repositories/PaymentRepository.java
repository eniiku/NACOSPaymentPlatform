package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
