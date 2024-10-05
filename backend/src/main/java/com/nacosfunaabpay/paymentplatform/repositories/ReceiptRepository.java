package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
}
