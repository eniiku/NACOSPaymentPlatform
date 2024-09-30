package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.model.Program;

import java.util.List;

public interface ProgramService {
    Program findProgram(String programName);
    List<Program> getAllPrograms();
}