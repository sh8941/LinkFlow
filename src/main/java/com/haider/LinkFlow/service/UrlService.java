package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.reponse.PageResponse;
import com.haider.LinkFlow.dtos.reponse.UrlWrapper;
import com.haider.LinkFlow.dtos.reponse.DashboardSummaryResponse;
import com.haider.LinkFlow.repo.UrlClickRepo;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
    @Autowired
    private UrlClickRepo  urlClickRepo; // todo: this is temporary used here later refactor

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
        urlResponse.setCreatedAt(savedUrl.getCreatedAt());
        urlResponse.setExpiresAt(savedUrl.getExpiresAt());
        urlResponse.setId(savedUrl.getId());
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

    public PageResponse getMyUrls(Pageable pageable) {
        UserEntity currentUser = securityUtils.getCurrentUser();
        List<UrlResponse> content = urlRepo.findByCreator_IdAndActiveTrue(currentUser.getId(),pageable).stream().map((e) ->
        {
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setOriginalUrl(e.getOriginalUrl());
            urlResponse.setClickCount(e.getClickCount());
            urlResponse.setShortCode(e.getShortCode());
            urlResponse.setCreator(e.getCreator().getId());
            urlResponse.setCreatedAt(e.getCreatedAt());
            urlResponse.setExpiresAt(e.getExpiresAt());
            urlResponse.setId(e.getId());
            return urlResponse;
        }).toList();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(content);
        pageResponse.setTotalPages(
            Math.ceilDiv(urlRepo.countByUserId(securityUtils.getCurrentUser().getId())
                    ,pageable.getPageSize()));
        pageResponse.setPageNumber(pageable.getPageNumber());
        pageResponse.setPageSize(pageable.getPageSize());
        return pageResponse;
    }

    public List<UrlWrapper> getTopUrlsActiveUrls() {
        UserEntity user = securityUtils.getCurrentUser();
        return
                urlRepo.findTop5ByCreator_IdAndActiveTrueOrderByClickCountDesc(user.getId())
                        .stream()
                        .map(e -> {
                            UrlWrapper urlWrapper = new UrlWrapper();
                            urlWrapper.setClicks(e.getClickCount());
                            urlWrapper.setCreatedAt(e.getCreatedAt());
                            urlWrapper.setOriginalUrl(e.getOriginalUrl());
                            urlWrapper.setShortLink(e.getShortCode());
                            return urlWrapper;
                        }).toList();
    }

    public DashboardSummaryResponse getSummary(LocalDate startDate, LocalDate endDate) {
        UserEntity user = securityUtils.getCurrentUser();
        Instant start = startDate
                .atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant();

        Instant end = endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant();
        DashboardSummaryResponse response = new DashboardSummaryResponse();

        response.setTotalLinks(urlRepo.countByUserIdBetweenDates(user.getId(), start, end));
        if (response.getTotalLinks() == 0) {
            return  response;
        }

        response.setUniqueClicks(urlClickRepo.countUniqueByIpAddress(user.getId(), start, end));
        response.setTotalClicks(urlClickRepo.clickCountByUserIdBetweenDate(user.getId(), start, end));
        response.setAverageClicks((float) response.getTotalClicks()/ (float) response.getTotalLinks());
        return response;
    }

    public DashboardSummaryResponse getSummaryOverAll() {
        UserEntity user = securityUtils.getCurrentUser();

        DashboardSummaryResponse response = new DashboardSummaryResponse();

        response.setTotalLinks(urlRepo.countByUserId(user.getId()));
        if (response.getTotalLinks() == 0) {
            return  response;
        }

        response.setUniqueClicks(urlClickRepo.countUniqueByIpAddress(user.getId()));
        response.setTotalClicks(urlRepo.clickCountByUserId(user.getId()));
        response.setAverageClicks((float) response.getTotalClicks()/ (float) response.getTotalLinks());
        return response;
    }
}
