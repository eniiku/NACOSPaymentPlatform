package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.model.AcademicYear;
import com.nacosfunaabpay.paymentplatform.repositories.AcademicYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Override
    @Transactional(readOnly = true)
    public AcademicYear getCurrentAcademicYear() {
        return academicYearRepository.findByIsCurrent(true)
                .orElseThrow(() -> new RuntimeException("No current academic year set"));
    }
}