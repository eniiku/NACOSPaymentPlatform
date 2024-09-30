package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Program;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends CrudRepository<Program, Long> {
    Optional<Program> findByName(String programName);
    List<Program> findAllByOrderByNameAsc();
}
