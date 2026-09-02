package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.reponse.PageResponse;
import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.entity.UrlEntity;
import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.exception.UrlExpiredException;
import com.haider.LinkFlow.repo.UrlClickRepo;
import com.haider.LinkFlow.repo.UrlRepo;
import com.haider.LinkFlow.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTests {

    @Mock
    private UrlRepo urlRepo;

    @Mock
    private ShortCodeUtility shortCodeUtility;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private UrlClickRepo urlClickRepo;

    @InjectMocks
    private UrlService urlService;

    @Test
    void addUrl_shouldReturnMappedResponse() {
        UrlRequest request = new UrlRequest();
        request.setLongUrl("https://example.com/");

        UserEntity currentUser = new UserEntity();
        currentUser.setId(7L);

        when(shortCodeUtility.normalize("https://example.com/")).thenReturn("https://example.com");
        when(shortCodeUtility.generateUniqueCode()).thenReturn("abc12345");
        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(urlRepo.save(any(UrlEntity.class))).thenAnswer(invocation -> {
            UrlEntity saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        UrlResponse response = urlService.addUrl(request);

        assertNotNull(response);
        assertEquals("https://example.com", response.getOriginalUrl());
        assertEquals("abc12345", response.getShortCode());
        assertEquals(7L, response.getCreator());
        assertEquals(0L, response.getClickCount());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getExpiresAt());
        verify(urlRepo).save(any(UrlEntity.class));
    }

    @Test
    void getByShortCode_shouldReturnOriginalUrlAndIncrementClickCount() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setOriginalUrl("https://example.com");
        urlEntity.setShortCode("abc12345");
        urlEntity.setClickCount(2L);
        urlEntity.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
        urlEntity.setActive(true);

        when(urlRepo.findByShortCodeAndActiveTrue("abc12345")).thenReturn(Optional.of(urlEntity));
        when(urlRepo.save(urlEntity)).thenReturn(urlEntity);

        String redirectUrl = urlService.getByShortCode("abc12345");

        assertEquals("https://example.com", redirectUrl);
        assertEquals(3L, urlEntity.getClickCount());
        verify(urlRepo).save(urlEntity);
    }

    @Test
    void getByShortCode_shouldThrowWhenUrlExpired() {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortCode("expired123");
        urlEntity.setOriginalUrl("https://expired.example");
        urlEntity.setExpiresAt(Instant.now().minusSeconds(60));
        urlEntity.setActive(true);

        when(urlRepo.findByShortCodeAndActiveTrue("expired123")).thenReturn(Optional.of(urlEntity));

        assertThrows(UrlExpiredException.class, () -> urlService.getByShortCode("expired123"));
        verify(urlRepo, never()).save(any(UrlEntity.class));
    }

    @Test
    void getMyUrls_shouldMapEntitiesToPageResponse() {
        UserEntity creator = new UserEntity();
        creator.setId(99L);

        when(securityUtils.getCurrentUser()).thenReturn(creator);

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setId(12L);
        urlEntity.setOriginalUrl("https://page.example.com");
        urlEntity.setShortCode("page123");
        urlEntity.setCreator(creator);
        urlEntity.setClickCount(15L);
        urlEntity.setCreatedAt(Instant.parse("2025-01-01T10:00:00Z"));
        urlEntity.setExpiresAt(Instant.parse("2025-01-08T10:00:00Z"));

        PageRequest pageRequest = PageRequest.of(0, 10);
        when(urlRepo.findByCreator_IdAndActiveTrue(99L, pageRequest)).thenReturn(List.of(urlEntity));
        when(urlRepo.countByUserId(99L)).thenReturn(1L);

        PageResponse response = urlService.getMyUrls(pageRequest);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getPageNumber());
        assertEquals(10, response.getPageSize());

        UrlResponse responseItem = (UrlResponse) response.getContent().get(0);
        assertEquals(12L, responseItem.getId());
        assertEquals("https://page.example.com", responseItem.getOriginalUrl());
        assertEquals("page123", responseItem.getShortCode());
        assertEquals(99L, responseItem.getCreator());
        assertEquals(15L, responseItem.getClickCount());
    }
}
