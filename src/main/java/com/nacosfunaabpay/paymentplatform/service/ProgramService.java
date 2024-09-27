package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.model.Program;

public interface ProgramService {
    Program findOrCreateProgram(String programName);
}