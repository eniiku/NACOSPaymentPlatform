package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.exceptions.ProgramNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.repositories.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramServiceImpl implements ProgramService {

    private static final Logger logger = LoggerFactory.getLogger(LevelServiceImpl.class);

    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional(readOnly = true)
    public Program findProgram(String programName) {
        return programRepository.findByName(programName).orElseThrow( () -> {
            logger.error("Level not found for ID: {}", programName);
            return new ProgramNotFoundException("Level not found for ID: " + programName);
        });

    }
}