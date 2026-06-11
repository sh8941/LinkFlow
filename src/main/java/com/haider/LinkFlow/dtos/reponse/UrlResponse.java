package com.haider.LinkFlow.dtos.reponse;

import lombok.Data;

@Data
public class UrlResponse {
    private String originalUrl;
    private String shortCode;
    private Long clickCount;
    private Long creator;
}
