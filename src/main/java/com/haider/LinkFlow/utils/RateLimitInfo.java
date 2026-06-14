package com.haider.LinkFlow.utils;

import lombok.Data;

@Data
public class RateLimitInfo {
    private int requestCount;
    private long windowStart;

    public RateLimitInfo() {
        this.requestCount = 1;
        this.windowStart = System.currentTimeMillis();
    }

    // getters and setters
}
