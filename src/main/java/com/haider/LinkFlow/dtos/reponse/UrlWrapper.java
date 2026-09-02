package com.haider.LinkFlow.dtos.reponse;

import lombok.Data;

import java.time.Instant;

@Data
public class UrlWrapper {
    private String originalUrl;
    private String shortLink;
    private Long clicks;
    private Instant createdAt;
}
