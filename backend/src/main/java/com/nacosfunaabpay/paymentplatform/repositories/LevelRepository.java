package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Level;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LevelRepository extends CrudRepository<Level, Long> {
    List<Level> findAllByOrderByNameAsc();
    Optional<Level> findByKey(String key);
}
