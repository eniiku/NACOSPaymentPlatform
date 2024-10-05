package com.nacosfunaabpay.paymentplatform.service;


import com.nacosfunaabpay.paymentplatform.exceptions.LevelNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.Level;
import com.nacosfunaabpay.paymentplatform.repositories.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Service
public class LevelServiceImpl implements LevelService {

    private static final Logger logger = LoggerFactory.getLogger(LevelServiceImpl.class);

    @Autowired
    private LevelRepository levelRepository;

    public Level findLevel(Long levelId) {
        return levelRepository.findById(levelId).orElseThrow(() -> {
            logger.error("Level not found for ID: {}", levelId);
            return new LevelNotFoundException("Level not found: " + levelId);
        });
    }

    public List<Level> getAllLevels() {
        return levelRepository.findAllByOrderByNameAsc();
    }
}