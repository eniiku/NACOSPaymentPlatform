package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.model.Level;

import java.util.List;

public interface LevelService {
    Level findLevel(Long levelId);
    List<Level> getAllLevels();
}