package com.haider.LinkFlow.service;

import com.haider.LinkFlow.repo.UrlRepo;
import com.haider.LinkFlow.utils.SecurityUtils;
import com.haider.LinkFlow.dtos.reponse.UrlClickResponse;
import com.haider.LinkFlow.entity.UrlClickEntity;
import com.haider.LinkFlow.entity.UrlEntity;
import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.repo.UrlClickRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class UrlClickService {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlClickRepo urlClickRepo;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UrlRepo urlRepo;

    public void trackClick(HttpServletRequest request, String url) {
        UrlClickEntity urlClickEntity = new UrlClickEntity();
        urlClickEntity.setClickedAt(Instant.now());
        urlClickEntity.setReferer(request.getHeader("referer"));
        urlClickEntity.setIpAddress(request.getRemoteAddr());
        urlClickEntity.setUserAgent(request.getHeader("user-agent"));
        urlClickEntity.setUrlEntity(urlService.getEntityByShortCode(url));
        urlClickRepo.save(urlClickEntity);
    }

    public List<UrlClickResponse> getUrlClicks(String url, Pageable pageable) {
        UrlEntity urlEntity = urlService.getEntityByShortCode(url);
        UserEntity currentUser = securityUtils.getCurrentUser();
        if (! urlEntity.getCreator().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("this url is not owned by you");
        }
        return urlClickRepo.findByUrlEntity(urlEntity, pageable).stream().map(urlClickEntity ->  {
            UrlClickResponse urlClickResponse = new UrlClickResponse();
            urlClickResponse.setUrlEntity(urlEntity.getId());
            urlClickResponse.setClickedAt(urlClickEntity.getClickedAt());
            urlClickResponse.setIpAddress(urlClickEntity.getIpAddress());
            urlClickResponse.setUserAgent(urlClickEntity.getUserAgent());
            urlClickResponse.setReferer(urlClickEntity.getReferer());
            return urlClickResponse;
        }).toList();
    }

    public List<UrlClickResponse> getMyUrlClicks(LocalDate startDate, LocalDate endDate) {

        Instant start = startDate
                .atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant();

        Instant end = endDate
                .plusDays(1)
                .atStartOfDay(ZoneId.of("Asia/Kolkata"))
                .toInstant();

        UserEntity user = securityUtils.getCurrentUser();

        List<UrlEntity> urls = urlRepo.findByCreator_IdAndActiveTrue(
                user.getId(),
                Pageable.unpaged()
        );

        return urls.stream()
                .flatMap(url -> urlClickRepo
                        .findByUrlEntityAndCreatedBetween(url, Pageable.unpaged(), start, end)
                        .stream()
                )
                .map(click -> new UrlClickResponse(
                        click.getId(),
                        click.getClickedAt(),
                        click.getIpAddress(),
                        click.getUserAgent(),
                        click.getReferer()
                ))
                .toList();
    }
}
