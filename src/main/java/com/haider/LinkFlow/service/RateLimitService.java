package com.haider.LinkFlow.service;

import com.haider.LinkFlow.utils.RateLimitInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_SIZE_MS = 60_000;

    private final ConcurrentHashMap<String, RateLimitInfo> store =
            new ConcurrentHashMap<>();

    public boolean allowRequest(String key) {
        long now = System.currentTimeMillis();

        RateLimitInfo info = store.compute(key, (k, existing) -> {

            if (existing == null) {
                return new RateLimitInfo();
            }

            if (now - existing.getWindowStart() > WINDOW_SIZE_MS) {
                existing.setWindowStart(now);
                existing.setRequestCount(1);
                return existing;
            }

            existing.setRequestCount(existing.getRequestCount() + 1);
            return existing;
        });

        return info.getRequestCount() <= MAX_REQUESTS;
    }
}
