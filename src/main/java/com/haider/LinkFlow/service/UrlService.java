package com.haider.LinkFlow.service;

import com.haider.LinkFlow.utils.SecurityUtils;
import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.entity.UrlEntity;
import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.exception.ResourceNotFound;
import com.haider.LinkFlow.exception.UrlExpiredException;
import com.haider.LinkFlow.repo.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class UrlService {
    @Autowired
    private UrlRepo urlRepo;
    @Autowired
    private ShortCodeUtility shortCodeUtility;
    @Autowired
    private SecurityUtils securityUtils;

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

        UserEntity currentUser = securityUtils.getCurrentUser();
        urlEntity.setCreator(currentUser);

        UrlEntity savedUrl = urlRepo.save(urlEntity);
        UrlResponse urlResponse = new UrlResponse();
        urlResponse.setOriginalUrl(savedUrl.getOriginalUrl());
        urlResponse.setClickCount(savedUrl.getClickCount());
        urlResponse.setShortCode(savedUrl.getShortCode());
        urlResponse.setCreator(savedUrl.getCreator().getId());
        return urlResponse;
    }

    public UrlEntity getEntityByShortCode(String shortCode) {
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

    public void deactivateUrl(String url) {
        UrlEntity urlEntity = getEntityByShortCode(url);
        UserEntity currentUser = securityUtils.getCurrentUser();
        if (! urlEntity.getCreator().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to deactivate this url");
        }
        urlEntity.setActive(false);
        urlRepo.save(urlEntity);
    }

    public List<UrlResponse> getMyUrls(Pageable pageable) {
        UserEntity currentUser = securityUtils.getCurrentUser();
        return urlRepo.findByCreator_IdAndActiveTrue(currentUser.getId(),pageable).stream().map((e) ->
        {
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setOriginalUrl(e.getOriginalUrl());
            urlResponse.setClickCount(e.getClickCount());
            urlResponse.setShortCode(e.getShortCode());
            urlResponse.setCreator(e.getCreator().getId());
            return urlResponse;
        }).toList();
    }
}
