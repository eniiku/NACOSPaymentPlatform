package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface RecieptRepository extends CrudRepository<Receipt, Long> {
}
