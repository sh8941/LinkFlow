package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UrlResponse;
import com.haider.LinkFlow.dtos.request.UrlRequest;
import com.haider.LinkFlow.service.UrlClickService;
import com.haider.LinkFlow.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/url")
public class UrlController {
    @Autowired
    private UrlService urlService;
    @Autowired
    private UrlClickService urlClickService;

    @PostMapping
    public ResponseEntity<?> createUrl(@RequestBody @Valid UrlRequest urlRequest) {
        UrlResponse urlResponse = urlService.addUrl(urlRequest);
        return ResponseEntity.ok(urlResponse);
    }

    @GetMapping("{url}")
    public ResponseEntity<?> getUrl(@PathVariable String url, HttpServletRequest request) {
        String longUrl = urlService.getByShortCode(url);
        urlClickService.trackClick(request,url);
        return ResponseEntity.status(301).location(URI.create(longUrl)).build();
    }

    @DeleteMapping("{url}")
    public ResponseEntity<?> deleteUrl(@PathVariable String url) {
        urlService.deactivateUrl(url);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    ResponseEntity<?> getMyUrls(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        List<UrlResponse> urls = urlService.getMyUrls(pageable);
        return ResponseEntity.ok(urls);
    }
}
