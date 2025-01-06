package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Payment;
import com.nacosfunaabpay.paymentplatform.model.Receipt;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    Optional<Receipt> findByPayment(Payment payment);
}
