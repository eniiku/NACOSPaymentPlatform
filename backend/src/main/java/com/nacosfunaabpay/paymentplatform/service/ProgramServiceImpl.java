package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.exceptions.ProgramNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.repositories.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {

    private static final Logger logger = LoggerFactory.getLogger(ProgramServiceImpl.class);

    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional(readOnly = true)
    public Program findProgram(String key) {

        return programRepository.findByKey(key).orElseThrow( () -> {
            logger.error("Level not found for ID: {}", key);
            return new ProgramNotFoundException("Program not found for program with key: " + key);
        });
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAllByOrderByNameAsc();
    }
}