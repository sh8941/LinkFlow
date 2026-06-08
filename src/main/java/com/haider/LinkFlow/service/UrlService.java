package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.entity.UrlEntity;
import com.haider.LinkFlow.exception.ResourceNotFound;
import com.haider.LinkFlow.exception.UrlExpiredException;
import com.haider.LinkFlow.repo.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;


@Service
public class UrlService {
    @Autowired
    private UrlRepo urlRepo;
    @Autowired
    private ShortCodeUtility shortCodeUtility;

    public UrlResponse addUrl(UrlRequest urlRequest) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setActive(true);
        String normalizedUrl = shortCodeUtility.normalize(urlRequest.getLongUrl());
        // check if already exist longUrl then throw exception
        urlEntity.setOriginalUrl(normalizedUrl);
        urlEntity.setClickCount(0L);
        urlEntity.setCreatedAt(Instant.now());
        urlEntity.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        urlEntity.setShortCode(shortCodeUtility.generateUniqueCode());
        UrlEntity savedUrl = urlRepo.save(urlEntity);
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(savedUrl.getOriginalUrl());
        urlResponse.setClickCount(savedUrl.getClickCount());
        urlResponse.setShortCode(savedUrl.getShortCode());
        return urlResponse;
    }

    private UrlEntity getEntityByShortCode(String shortCode) {
        return urlRepo.findByShortCodeAndActiveTrue(shortCode).orElseThrow(() ->
                new ResourceNotFound("url not found"));
    }

    public String getByShortCode(String shortCode) {
        UrlEntity urlEntity = getEntityByShortCode(shortCode);
        if (urlEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new UrlExpiredException("short url has expired");
        }

        urlEntity.setClickCount(urlEntity.getClickCount() + 1);
        urlRepo.save(urlEntity);
        return urlEntity.getOriginalUrl();
    }

}
