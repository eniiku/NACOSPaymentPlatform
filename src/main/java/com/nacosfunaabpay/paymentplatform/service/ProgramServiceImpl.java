package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional(readOnly = true)
    public Program findOrCreateProgram(String programName) {
        return programRepository.findByName(programName)
                .orElseGet(() -> {
                    Program newProgram = new Program();
                    newProgram.setName(programName);
                    return programRepository.save(newProgram);
                });
    }
}