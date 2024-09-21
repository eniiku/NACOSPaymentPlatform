package com.nacosfunaabpay.paymentplatform.config;

import com.nacosfunaabpay.paymentplatform.model.AcademicYear;
import com.nacosfunaabpay.paymentplatform.repositories.AcademicYearRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializationConfig {
    @Bean
    public CommandLineRunner initData(AcademicYearRepository academicYearRepository) {
        return args -> {
            if (academicYearRepository.count() == 0) {
                LocalDate now = LocalDate.now();
                AcademicYear currentYear = new AcademicYear();
                currentYear.setYear(now.getYear() + "/" + (now.getYear() + 1));
                //               INFO: Set start of academic year
                currentYear.setStartDate(LocalDate.of(now.getYear(), 9, 1));
                //               INFO: Set end of academic year
                currentYear.setEndDate(LocalDate.of(now.getYear() + 1, 8, 31));
                currentYear.setCurrent(true);
                academicYearRepository.save(currentYear);
            }
        };
    }
}
