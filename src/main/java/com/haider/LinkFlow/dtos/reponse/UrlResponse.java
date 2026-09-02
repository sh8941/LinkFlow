package com.haider.LinkFlow.dtos.reponse;

import lombok.Data;

import java.time.Instant;

@Data
public class UrlResponse {
    private Long id;
    private String originalUrl;
    private String shortCode;
    private Long creator;
    private Long clickCount;
    private Instant createdAt;
    private Instant expiresAt;
}
