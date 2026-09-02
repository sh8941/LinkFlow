package com.haider.LinkFlow.dtos.reponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlClickResponse {
    private Long urlEntity;
    private Instant clickedAt;
    private String ipAddress;
    private String userAgent;
    private String referer;
}
