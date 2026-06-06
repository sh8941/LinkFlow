package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.entity.UrlEntity;
import com.haider.LinkFlow.exception.ResourceNotFound;
import com.haider.LinkFlow.repo.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UrlService {
    @Autowired
    private UrlRepo urlRepo;
    @Autowired
    private ShortCodeUtility shortCodeUtility;

    public UrlResponse addUrl(UrlRequest urlRequest) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setActive(true);
        urlEntity.setOriginalUrl(urlRequest.getLongUrl());
        urlEntity.setClickCount(0L);
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
        urlEntity.setClickCount(urlEntity.getClickCount() + 1);
        urlRepo.save(urlEntity);
        return urlEntity.getOriginalUrl();
    }

}
