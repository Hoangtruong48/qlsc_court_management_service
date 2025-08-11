package com.example.courtmanagement_service.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class IdempotencyService {
    private final Cache<String, Boolean> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MILLISECONDS)
            .build();

    public boolean checkAndLog(String requestId) {
        return cache.asMap().putIfAbsent(requestId, true) == null;
    }
}
