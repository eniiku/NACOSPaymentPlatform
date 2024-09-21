package com.nacosfunaabpay.paymentplatform.repositories;

import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByRegistrationNumber(String registrationNumber);
}
