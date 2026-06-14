package com.haider.LinkFlow.dtos.reponse;


import lombok.Data;

import java.time.Instant;

@Data
public class UrlClickResponse {
    private Long urlEntity;
    private Instant clickedAt;
    private String ipAddress;
    private String userAgent;
    private String referer;
}
