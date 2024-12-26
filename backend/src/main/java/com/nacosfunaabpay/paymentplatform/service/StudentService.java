package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.model.Student;

public interface StudentService {

    Student handleStudentRegistration(PaymentFormDTO paymentForm);

}
