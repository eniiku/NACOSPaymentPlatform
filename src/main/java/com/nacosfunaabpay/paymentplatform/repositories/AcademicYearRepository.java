package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.AcademicYear;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AcademicYearRepository extends CrudRepository<AcademicYear, Long> {
    Optional<AcademicYear> findByIsCurrent(boolean isCurrent);
}
