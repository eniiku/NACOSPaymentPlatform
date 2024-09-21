package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.model.Student;

public interface StudentService {

    Student createOrUpdateStudent(Student student);

    Student findStudentByEmail(String email);

    Student findStudentByRegistrationNumber(String registrationNumber);

    Student createStudent(PaymentFormDTO paymentForm);
}
